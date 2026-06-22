async fn list_applications(
    State(state): State<AppState>,
    Query(q): Query<HashMap<String, String>>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let mut where_sql = "1=1".to_string();
    let mut vals: Vec<String> = vec![];
    if let Some(v) = q.get("positionId").filter(|s| !s.is_empty()) {
        where_sql.push_str(" AND ja.position_id=?");
        vals.push(v.clone());
    }
    if let Some(v) = q.get("status").filter(|s| !s.is_empty()) {
        where_sql.push_str(" AND ja.status=?");
        vals.push(v.clone());
    }
    let total: i64 = conn
        .query_row(
            &format!("SELECT COUNT(*) FROM job_application ja WHERE {where_sql}"),
            params_from_iter(vals.iter()),
            |r| r.get(0),
        )
        .unwrap_or(0);
    let (limit, offset) = page(&q);
    vals.push(limit.to_string());
    vals.push(offset.to_string());
    let rows = query_values(&conn, &format!("SELECT ja.id,ja.position_id,ja.resume_id,ja.status,ja.operate_time,ja.operator_id,ja.remark,ja.created_time,jp.title,rm.name FROM job_application ja LEFT JOIN job_position jp ON jp.id=ja.position_id LEFT JOIN resume_main rm ON rm.id=ja.resume_id WHERE {where_sql} ORDER BY ja.id DESC LIMIT ? OFFSET ?"), params_from_iter(vals.iter()), application_row)?;
    Ok(ok(table(rows, total)))
}

fn application_row(r: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(
        json!({"id": r.get::<_, i64>(0)?, "positionId": r.get::<_, i64>(1)?, "resumeId": r.get::<_, i64>(2)?, "status": r.get::<_, String>(3)?, "operateTime": r.get::<_, String>(4)?, "operatorId": r.get::<_, Option<i64>>(5)?, "remark": r.get::<_, Option<String>>(6)?, "createdTime": r.get::<_, String>(7)?, "positionTitle": r.get::<_, Option<String>>(8)?, "resumeName": r.get::<_, Option<String>>(9)?}),
    )
}

async fn get_application(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    Ok(ok(
        get_application_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "投递记录不存在"))?
    ))
}

fn get_application_value(conn: &Connection, id: i64) -> ApiResult<Option<Value>> {
    conn.prepare("SELECT ja.id,ja.position_id,ja.resume_id,ja.status,ja.operate_time,ja.operator_id,ja.remark,ja.created_time,jp.title,rm.name FROM job_application ja LEFT JOIN job_position jp ON jp.id=ja.position_id LEFT JOIN resume_main rm ON rm.id=ja.resume_id WHERE ja.id=?")
        .map_err(|_| ApiError::new(500, "查询投递失败"))?
        .query_row(params![id], application_row).optional().map_err(|_| ApiError::new(500, "查询投递失败"))
}

async fn create_application(
    State(state): State<AppState>,
    headers: HeaderMap,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let position_id =
        json_i64(&body, "positionId").ok_or_else(|| ApiError::new(400, "职位ID不能为空"))?;
    let resume_id =
        json_i64(&body, "resumeId").ok_or_else(|| ApiError::new(400, "简历ID不能为空"))?;
    let job = get_job_value(&conn, position_id)?
        .ok_or_else(|| ApiError::new(400, "该职位已关闭，无法创建投递"))?;
    if job["status"].as_str() != Some("OPEN") {
        return Err(ApiError::new(400, "该职位已关闭，无法创建投递"));
    }
    if conn
        .query_row(
            "SELECT id FROM job_application WHERE position_id=? AND resume_id=?",
            params![position_id, resume_id],
            |r| r.get::<_, i64>(0),
        )
        .optional()
        .unwrap_or(None)
        .is_some()
    {
        return Err(ApiError::new(409, "该简历已投递此职位"));
    }
    conn.execute("INSERT INTO job_application(position_id,resume_id,status,operate_time,operator_id,remark,created_time) VALUES(?,?,'PENDING',?,?,?,?)",
        params![position_id, resume_id, now(), current_user_id(&headers), json_text(&body, "remark"), now()]).map_err(|_| ApiError::new(500, "创建投递失败"))?;
    let id = conn.last_insert_rowid();
    Ok(ok(get_application_value(&conn, id)?.unwrap()))
}

async fn update_application_status(
    State(state): State<AppState>,
    headers: HeaderMap,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    change_application_status(
        &conn,
        id,
        &json_text(&body, "status").unwrap_or_default(),
        json_text(&body, "remark"),
        current_user_id(&headers),
        "System Admin",
    )?;
    Ok(ok_empty())
}

fn change_application_status(
    conn: &Connection,
    id: i64,
    to_status: &str,
    remark: Option<String>,
    operator_id: i64,
    operator_name: &str,
) -> ApiResult<()> {
    let app =
        get_application_value(conn, id)?.ok_or_else(|| ApiError::new(404, "投递记录不存在"))?;
    let from = app["status"].as_str().unwrap_or("");
    let valid = [
        "PENDING:RESUME_PASSED",
        "PENDING:RESUME_REJECTED",
        "RESUME_PASSED:INTERVIEW_WAITING",
        "INTERVIEW_WAITING:INTERVIEWING",
        "INTERVIEWING:INTERVIEW_PASSED",
        "INTERVIEWING:INTERVIEW_REJECTED",
        "INTERVIEW_PASSED:HIRED",
    ];
    if !valid.contains(&format!("{from}:{to_status}").as_str()) {
        return Err(ApiError::new(
            400,
            format!("非法状态流转: {from} -> {to_status}"),
        ));
    }
    conn.execute(
        "UPDATE job_application SET status=?,operate_time=?,operator_id=?,remark=? WHERE id=?",
        params![to_status, now(), operator_id, remark, id],
    )
    .ok();
    conn.execute("INSERT INTO application_log(application_id,from_status,to_status,operator_id,operator_name,remark,created_time) VALUES(?,?,?,?,?,?,?)",
        params![id, from, to_status, operator_id, operator_name, remark, now()]).ok();
    Ok(())
}

async fn application_logs(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    Ok(ok(json!(query_values(&conn, "SELECT id,application_id,from_status,to_status,operator_id,operator_name,remark,created_time FROM application_log WHERE application_id=? ORDER BY id", params![id], |r| Ok(json!({"id": r.get::<_, i64>(0)?, "applicationId": r.get::<_, i64>(1)?, "fromStatus": r.get::<_, Option<String>>(2)?, "toStatus": r.get::<_, String>(3)?, "operatorId": r.get::<_, Option<i64>>(4)?, "operatorName": r.get::<_, Option<String>>(5)?, "remark": r.get::<_, Option<String>>(6)?, "createdTime": r.get::<_, String>(7)?})))?)))
}
