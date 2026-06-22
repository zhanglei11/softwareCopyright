async fn list_interviews(
    State(state): State<AppState>,
    Query(q): Query<HashMap<String, String>>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let mut where_sql = "1=1".to_string();
    let mut vals = vec![];
    if let Some(v) = q.get("interviewer").filter(|s| !s.is_empty()) {
        where_sql.push_str(" AND interviewer LIKE ?");
        vals.push(format!("%{}%", v));
    }
    let total: i64 = conn
        .query_row(
            &format!("SELECT COUNT(*) FROM interview_record WHERE {where_sql}"),
            params_from_iter(vals.iter()),
            |r| r.get(0),
        )
        .unwrap_or(0);
    let (limit, offset) = page(&q);
    vals.push(limit.to_string());
    vals.push(offset.to_string());
    let rows = query_values(&conn, &format!("SELECT id,application_id,interview_time,interviewer,location,score,comment,result,created_time FROM interview_record WHERE {where_sql} ORDER BY id DESC LIMIT ? OFFSET ?"), params_from_iter(vals.iter()), interview_row)?;
    Ok(ok(table(rows, total)))
}

fn interview_row(r: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(
        json!({"id": r.get::<_, i64>(0)?, "applicationId": r.get::<_, i64>(1)?, "interviewTime": r.get::<_, Option<String>>(2)?, "interviewer": r.get::<_, Option<String>>(3)?, "location": r.get::<_, Option<String>>(4)?, "score": r.get::<_, Option<i64>>(5)?, "comment": r.get::<_, Option<String>>(6)?, "result": r.get::<_, Option<String>>(7)?, "createdTime": r.get::<_, String>(8)?}),
    )
}

async fn get_interview(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    Ok(ok(
        get_interview_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "面试记录不存在"))?
    ))
}

fn get_interview_value(conn: &Connection, id: i64) -> ApiResult<Option<Value>> {
    conn.prepare("SELECT id,application_id,interview_time,interviewer,location,score,comment,result,created_time FROM interview_record WHERE id=?").map_err(|_| ApiError::new(500, "查询面试失败"))?.query_row(params![id], interview_row).optional().map_err(|_| ApiError::new(500, "查询面试失败"))
}

async fn create_interview(
    State(state): State<AppState>,
    headers: HeaderMap,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let app_id =
        json_i64(&body, "applicationId").ok_or_else(|| ApiError::new(400, "投递ID不能为空"))?;
    let app = get_application_value(&conn, app_id)?
        .ok_or_else(|| ApiError::new(404, "投递记录不存在"))?;
    let status = app["status"].as_str().unwrap_or("");
    if status != "INTERVIEW_WAITING" && status != "INTERVIEWING" {
        return Err(ApiError::new(400, "当前投递状态不允许创建面试"));
    }
    conn.execute("INSERT INTO interview_record(application_id,interview_time,interviewer,location,created_time) VALUES(?,?,?,?,?)",
        params![app_id, json_text(&body, "interviewTime"), json_text(&body, "interviewer"), json_text(&body, "location"), now()]).map_err(|_| ApiError::new(500, "创建面试失败"))?;
    let interview_id = conn.last_insert_rowid();
    if status == "INTERVIEW_WAITING" {
        change_application_status(
            &conn,
            app_id,
            "INTERVIEWING",
            Some("创建面试后进入面试中".into()),
            current_user_id(&headers),
            "System Admin",
        )?;
    }
    Ok(ok(get_interview_value(&conn, interview_id)?.unwrap()))
}

async fn update_interview(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    conn.execute(
        "UPDATE interview_record SET interview_time=?,interviewer=?,location=? WHERE id=?",
        params![
            json_text(&body, "interviewTime"),
            json_text(&body, "interviewer"),
            json_text(&body, "location"),
            id
        ],
    )
    .map_err(|_| ApiError::new(500, "更新面试失败"))?;
    Ok(ok_empty())
}

async fn update_interview_result(
    State(state): State<AppState>,
    headers: HeaderMap,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let record =
        get_interview_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "面试记录不存在"))?;
    let result =
        json_text(&body, "result").ok_or_else(|| ApiError::new(400, "面试结果不能为空"))?;
    conn.execute(
        "UPDATE interview_record SET score=?,comment=?,result=? WHERE id=?",
        params![
            json_i32(&body, "score"),
            json_text(&body, "comment"),
            result,
            id
        ],
    )
    .ok();
    let new_status = if result == "PASS" {
        "INTERVIEW_PASSED"
    } else {
        "INTERVIEW_REJECTED"
    };
    change_application_status(
        &conn,
        record["applicationId"].as_i64().unwrap_or(0),
        new_status,
        Some(format!("面试结果：{result}")),
        current_user_id(&headers),
        "System Admin",
    )?;
    Ok(ok_empty())
}
