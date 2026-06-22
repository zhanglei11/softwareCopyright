async fn list_jobs(
    State(state): State<AppState>,
    Query(q): Query<HashMap<String, String>>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    list_simple(
        &conn,
        "job_position",
        job_row,
        &q,
        &[
            ("title", "title"),
            ("department", "department"),
            ("status", "status"),
            ("jobType", "job_type"),
            ("eduRequire", "edu_require"),
        ],
        "deleted=0",
        "id DESC",
    )
}

fn job_row(row: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(
        json!({"id": row.get::<_, i64>(0)?, "title": row.get::<_, String>(1)?, "department": row.get::<_, Option<String>>(2)?, "jobType": row.get::<_, Option<String>>(3)?, "location": row.get::<_, Option<String>>(4)?, "salaryMin": row.get::<_, Option<i64>>(5)?, "salaryMax": row.get::<_, Option<i64>>(6)?, "eduRequire": row.get::<_, Option<String>>(7)?, "expRequire": row.get::<_, Option<i64>>(8)?, "description": row.get::<_, Option<String>>(9)?, "skillTags": serde_json::from_str::<Value>(&row.get::<_, Option<String>>(10)?.unwrap_or_else(|| "[]".into())).unwrap_or(json!([])), "status": row.get::<_, String>(11)?, "deleted": row.get::<_, i64>(12)?, "creatorId": row.get::<_, Option<i64>>(13)?, "createdTime": row.get::<_, String>(14)?, "updatedTime": row.get::<_, String>(15)?}),
    )
}

async fn get_job(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    Ok(ok(
        get_job_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "职位不存在"))?
    ))
}

fn get_job_value(conn: &Connection, id: i64) -> ApiResult<Option<Value>> {
    conn.prepare("SELECT id,title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time FROM job_position WHERE id=? AND deleted=0")
        .map_err(|_| ApiError::new(500, "查询职位失败"))?
        .query_row(params![id], job_row).optional().map_err(|_| ApiError::new(500, "查询职位失败"))
}

async fn create_job(
    State(state): State<AppState>,
    headers: HeaderMap,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    upsert_job(&conn, None, &body, current_user_id(&headers))?;
    Ok(ok_empty())
}

async fn update_job(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let job = get_job_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "职位不存在"))?;
    if job["status"].as_str() == Some("CLOSED") {
        return Err(ApiError::new(400, "已关闭职位不可编辑"));
    }
    upsert_job(&conn, Some(id), &body, 1)?;
    Ok(ok_empty())
}

fn upsert_job(conn: &Connection, id: Option<i64>, body: &Value, creator_id: i64) -> ApiResult<()> {
    let skills = body
        .get("skillTags")
        .cloned()
        .unwrap_or(json!([]))
        .to_string();
    if let Some(id) = id {
        conn.execute("UPDATE job_position SET title=?,department=?,job_type=?,location=?,salary_min=?,salary_max=?,edu_require=?,exp_require=?,description=?,skill_tags=?,updated_time=? WHERE id=?",
            params![json_text(body, "title").unwrap_or_default(), json_text(body, "department"), json_text(body, "jobType"), json_text(body, "location"), json_i32(body, "salaryMin"), json_i32(body, "salaryMax"), json_text(body, "eduRequire"), json_i32(body, "expRequire"), json_text(body, "description"), skills, now(), id])
            .map_err(|_| ApiError::new(500, "保存职位失败"))?;
    } else {
        conn.execute("INSERT INTO job_position(title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time) VALUES(?,?,?,?,?,?,?,?,?,?,'DRAFT',0,?,?,?)",
            params![json_text(body, "title").unwrap_or_default(), json_text(body, "department"), json_text(body, "jobType"), json_text(body, "location"), json_i32(body, "salaryMin"), json_i32(body, "salaryMax"), json_text(body, "eduRequire"), json_i32(body, "expRequire"), json_text(body, "description"), skills, creator_id, now(), now()])
            .map_err(|_| ApiError::new(500, "保存职位失败"))?;
    }
    Ok(())
}

async fn delete_job(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let job = get_job_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "职位不存在"))?;
    if job["status"].as_str() != Some("DRAFT") {
        return Err(ApiError::new(400, "仅 DRAFT 状态可删除"));
    }
    conn.execute(
        "UPDATE job_position SET deleted=1,updated_time=? WHERE id=?",
        params![now(), id],
    )
    .ok();
    Ok(ok_empty())
}

async fn publish_job(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    set_job_status(&state, id, "OPEN", Some("职位已是发布状态")).await
}

async fn close_job(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let job = get_job_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "职位不存在"))?;
    if job["status"].as_str() != Some("OPEN") {
        return Err(ApiError::new(400, "只有发布中的职位可关闭"));
    }
    conn.execute(
        "UPDATE job_position SET status='CLOSED',updated_time=? WHERE id=?",
        params![now(), id],
    )
    .ok();
    Ok(ok_empty())
}

async fn set_job_status(
    state: &AppState,
    id: i64,
    status: &str,
    duplicate_msg: Option<&str>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(state)?;
    let job = get_job_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "职位不存在"))?;
    if job["status"].as_str() == Some(status) {
        if let Some(msg) = duplicate_msg {
            return Err(ApiError::new(400, msg));
        }
    }
    conn.execute(
        "UPDATE job_position SET status=?,updated_time=? WHERE id=?",
        params![status, now(), id],
    )
    .ok();
    Ok(ok_empty())
}
