async fn list_resumes(
    State(state): State<AppState>,
    Query(q): Query<HashMap<String, String>>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let mut where_sql = "deleted=0".to_string();
    let mut vals: Vec<String> = vec![];
    for (param, col) in [
        ("name", "name"),
        ("phone", "phone"),
        ("highestEdu", "highest_edu"),
        ("source", "source"),
    ] {
        if let Some(v) = q.get(param).filter(|s| !s.is_empty()) {
            where_sql.push_str(&format!(" AND {col} LIKE ?"));
            vals.push(format!("%{}%", v));
        }
    }
    if let Some(skill) = q.get("skill").filter(|s| !s.is_empty()) {
        where_sql
            .push_str(" AND id IN (SELECT resume_id FROM resume_skill WHERE skill_name LIKE ?)");
        vals.push(format!("%{}%", skill));
    }
    let total: i64 = conn
        .query_row(
            &format!("SELECT COUNT(*) FROM resume_main WHERE {where_sql}"),
            params_from_iter(vals.iter()),
            |r| r.get(0),
        )
        .unwrap_or(0);
    let (limit, offset) = page(&q);
    vals.push(limit.to_string());
    vals.push(offset.to_string());
    let rows = query_values(&conn, &format!("SELECT id,name,phone,email,gender,birth_date,city,desired_position,desired_city,desired_salary_min,desired_salary_max,job_status,highest_edu,total_exp_years,file_path,parse_success,source,self_intro,deleted,creator_id,created_time,updated_time FROM resume_main WHERE {where_sql} ORDER BY id DESC LIMIT ? OFFSET ?"), params_from_iter(vals.iter()), resume_row)?;
    let rows = rows
        .into_iter()
        .map(|v| fill_resume(&conn, v))
        .collect::<ApiResult<Vec<_>>>()?;
    Ok(ok(table(rows, total)))
}

fn resume_row(row: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(
        json!({"id": row.get::<_, i64>(0)?, "name": row.get::<_, Option<String>>(1)?, "phone": row.get::<_, Option<String>>(2)?, "email": row.get::<_, Option<String>>(3)?, "gender": row.get::<_, Option<i64>>(4)?, "birthDate": row.get::<_, Option<String>>(5)?, "city": row.get::<_, Option<String>>(6)?, "desiredPosition": row.get::<_, Option<String>>(7)?, "desiredCity": row.get::<_, Option<String>>(8)?, "desiredSalaryMin": row.get::<_, Option<i64>>(9)?, "desiredSalaryMax": row.get::<_, Option<i64>>(10)?, "jobStatus": row.get::<_, Option<String>>(11)?, "highestEdu": row.get::<_, Option<String>>(12)?, "totalExpYears": row.get::<_, Option<i64>>(13)?, "filePath": row.get::<_, Option<String>>(14)?, "parseSuccess": row.get::<_, Option<i64>>(15)?, "source": row.get::<_, Option<String>>(16)?, "selfIntro": row.get::<_, Option<String>>(17)?, "deleted": row.get::<_, i64>(18)?, "creatorId": row.get::<_, Option<i64>>(19)?, "createdTime": row.get::<_, String>(20)?, "updatedTime": row.get::<_, String>(21)?}),
    )
}

async fn get_resume(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let v = get_resume_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "简历不存在"))?;
    Ok(ok(fill_resume(&conn, v)?))
}

fn get_resume_value(conn: &Connection, id: i64) -> ApiResult<Option<Value>> {
    conn.prepare("SELECT id,name,phone,email,gender,birth_date,city,desired_position,desired_city,desired_salary_min,desired_salary_max,job_status,highest_edu,total_exp_years,file_path,parse_success,source,self_intro,deleted,creator_id,created_time,updated_time FROM resume_main WHERE id=? AND deleted=0")
        .map_err(|_| ApiError::new(500, "查询简历失败"))?
        .query_row(params![id], resume_row).optional().map_err(|_| ApiError::new(500, "查询简历失败"))
}

fn fill_resume(conn: &Connection, mut v: Value) -> ApiResult<Value> {
    let id = v["id"].as_i64().unwrap_or(0);
    if let Value::Object(ref mut m) = v {
        m.insert("skills".into(), json!(query_values(conn, "SELECT id,resume_id,skill_name FROM resume_skill WHERE resume_id=? ORDER BY id", params![id], |r| Ok(json!({"id": r.get::<_, i64>(0)?, "resumeId": r.get::<_, i64>(1)?, "skillName": r.get::<_, String>(2)?})))?));
        m.insert("educations".into(), json!(query_values(conn, "SELECT id,resume_id,school,major,edu_level,start_date,end_date FROM resume_education WHERE resume_id=? ORDER BY id", params![id], |r| Ok(json!({"id": r.get::<_, i64>(0)?, "resumeId": r.get::<_, i64>(1)?, "school": r.get::<_, Option<String>>(2)?, "major": r.get::<_, Option<String>>(3)?, "eduLevel": r.get::<_, Option<String>>(4)?, "startDate": r.get::<_, Option<String>>(5)?, "endDate": r.get::<_, Option<String>>(6)?})))?));
        m.insert("workExps".into(), json!(query_values(conn, "SELECT id,resume_id,company,position,industry,start_date,end_date,description FROM resume_work_exp WHERE resume_id=? ORDER BY id", params![id], |r| Ok(json!({"id": r.get::<_, i64>(0)?, "resumeId": r.get::<_, i64>(1)?, "company": r.get::<_, Option<String>>(2)?, "position": r.get::<_, Option<String>>(3)?, "industry": r.get::<_, Option<String>>(4)?, "startDate": r.get::<_, Option<String>>(5)?, "endDate": r.get::<_, Option<String>>(6)?, "description": r.get::<_, Option<String>>(7)?})))?));
    }
    Ok(v)
}

async fn create_resume(
    State(state): State<AppState>,
    headers: HeaderMap,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let id = upsert_resume(&conn, None, &body, current_user_id(&headers))?;
    Ok(ok(fill_resume(
        &conn,
        get_resume_value(&conn, id)?.unwrap(),
    )?))
}

async fn update_resume(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    get_resume_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "简历不存在"))?;
    upsert_resume(&conn, Some(id), &body, 1)?;
    Ok(ok_empty())
}

fn upsert_resume(
    conn: &Connection,
    id: Option<i64>,
    body: &Value,
    creator_id: i64,
) -> ApiResult<i64> {
    let highest_edu = body
        .get("educations")
        .and_then(Value::as_array)
        .and_then(|xs| {
            xs.iter()
                .filter_map(|e| json_text(e, "eduLevel"))
                .max_by_key(|e| edu_level(e))
        })
        .or_else(|| json_text(body, "highestEdu"));
    let total_exp = body
        .get("workExps")
        .and_then(Value::as_array)
        .map(|xs| xs.len() as i32)
        .or_else(|| json_i32(body, "totalExpYears"));
    if let Some(id) = id {
        conn.execute("UPDATE resume_main SET name=?,phone=?,email=?,gender=?,birth_date=?,city=?,desired_position=?,desired_city=?,desired_salary_min=?,desired_salary_max=?,job_status=?,highest_edu=?,total_exp_years=?,source=?,self_intro=?,updated_time=? WHERE id=?",
            params![json_text(body, "name"), json_text(body, "phone"), json_text(body, "email"), json_i32(body, "gender"), json_text(body, "birthDate"), json_text(body, "city"), json_text(body, "desiredPosition"), json_text(body, "desiredCity"), json_i32(body, "desiredSalaryMin"), json_i32(body, "desiredSalaryMax"), json_text(body, "jobStatus"), highest_edu, total_exp, json_text(body, "source").unwrap_or_else(|| "MANUAL".into()), json_text(body, "selfIntro"), now(), id])
            .map_err(|_| ApiError::new(500, "保存简历失败"))?;
        replace_resume_children(conn, id, body)?;
        Ok(id)
    } else {
        conn.execute("INSERT INTO resume_main(name,phone,email,gender,birth_date,city,desired_position,desired_city,desired_salary_min,desired_salary_max,job_status,highest_edu,total_exp_years,parse_success,source,self_intro,deleted,creator_id,created_time,updated_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,0,?,?,?)",
            params![json_text(body, "name"), json_text(body, "phone"), json_text(body, "email"), json_i32(body, "gender"), json_text(body, "birthDate"), json_text(body, "city"), json_text(body, "desiredPosition"), json_text(body, "desiredCity"), json_i32(body, "desiredSalaryMin"), json_i32(body, "desiredSalaryMax"), json_text(body, "jobStatus"), highest_edu, total_exp, json_text(body, "source").unwrap_or_else(|| "MANUAL".into()), json_text(body, "selfIntro"), creator_id, now(), now()])
            .map_err(|_| ApiError::new(500, "保存简历失败"))?;
        let id = conn.last_insert_rowid();
        replace_resume_children(conn, id, body)?;
        Ok(id)
    }
}

fn replace_resume_children(conn: &Connection, id: i64, body: &Value) -> ApiResult<()> {
    conn.execute("DELETE FROM resume_skill WHERE resume_id=?", params![id])
        .ok();
    conn.execute(
        "DELETE FROM resume_education WHERE resume_id=?",
        params![id],
    )
    .ok();
    conn.execute("DELETE FROM resume_work_exp WHERE resume_id=?", params![id])
        .ok();
    if let Some(skills) = body.get("skills").and_then(Value::as_array) {
        for s in skills {
            let name = s
                .as_str()
                .map(str::to_string)
                .or_else(|| json_text(s, "skillName"))
                .unwrap_or_default();
            if !name.is_empty() {
                conn.execute(
                    "INSERT INTO resume_skill(resume_id,skill_name) VALUES(?,?)",
                    params![id, name],
                )
                .ok();
            }
        }
    }
    if let Some(items) = body.get("educations").and_then(Value::as_array) {
        for e in items {
            conn.execute("INSERT INTO resume_education(resume_id,school,major,edu_level,start_date,end_date) VALUES(?,?,?,?,?,?)",
                params![id, json_text(e, "school"), json_text(e, "major"), json_text(e, "eduLevel"), json_text(e, "startDate"), json_text(e, "endDate")]).ok();
        }
    }
    if let Some(items) = body.get("workExps").and_then(Value::as_array) {
        for w in items {
            conn.execute("INSERT INTO resume_work_exp(resume_id,company,position,industry,start_date,end_date,description) VALUES(?,?,?,?,?,?,?)",
                params![id, json_text(w, "company"), json_text(w, "position"), json_text(w, "industry"), json_text(w, "startDate"), json_text(w, "endDate"), json_text(w, "description")]).ok();
        }
    }
    Ok(())
}

async fn delete_resume(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    conn.execute(
        "UPDATE resume_main SET deleted=1,updated_time=? WHERE id=?",
        params![now(), id],
    )
    .ok();
    Ok(ok_empty())
}

async fn upload_resume(
    State(state): State<AppState>,
    headers: HeaderMap,
    mut multipart: Multipart,
) -> ApiResult<Json<Ajax<Value>>> {
    let mut stored_name = None;
    while let Some(field) = multipart
        .next_field()
        .await
        .map_err(|_| ApiError::new(400, "上传文件读取失败"))?
    {
        if field.name() == Some("file") {
            let filename = field.file_name().unwrap_or("resume.bin").to_string();
            let lower = filename.to_lowercase();
            if !(lower.ends_with(".pdf") || lower.ends_with(".doc") || lower.ends_with(".docx")) {
                return Err(ApiError::new(400, "仅支持 PDF、DOC、DOCX 格式"));
            }
            let ext = filename
                .rsplit_once('.')
                .map(|(_, e)| format!(".{e}"))
                .unwrap_or_default();
            let name = format!("{}{}", Uuid::new_v4(), ext);
            let bytes = field
                .bytes()
                .await
                .map_err(|_| ApiError::new(400, "上传文件读取失败"))?;
            if bytes.len() > 10 * 1024 * 1024 {
                return Err(ApiError::new(400, "文件大小不能超过 10MB"));
            }
            tokio::fs::write(state.upload_dir.join(&name), bytes)
                .await
                .map_err(|_| ApiError::new(500, "文件保存失败"))?;
            stored_name = Some(name);
        }
    }
    let file_path = stored_name.ok_or_else(|| ApiError::new(400, "文件不能为空"))?;
    let conn = db(&state)?;
    conn.execute("INSERT INTO resume_main(file_path,parse_success,source,deleted,creator_id,created_time,updated_time) VALUES(?,0,'FILE',0,?,?,?)",
        params![file_path, current_user_id(&headers), now(), now()]).map_err(|_| ApiError::new(500, "保存简历失败"))?;
    let id = conn.last_insert_rowid();
    Ok(ok(fill_resume(
        &conn,
        get_resume_value(&conn, id)?.unwrap(),
    )?))
}

async fn download_resume_file(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Response> {
    let name = {
        let conn = db(&state)?;
        let resume =
            get_resume_value(&conn, id)?.ok_or_else(|| ApiError::new(404, "简历不存在"))?;
        resume["filePath"]
            .as_str()
            .ok_or_else(|| ApiError::new(404, "无附件"))?
            .to_string()
    };
    let bytes = tokio::fs::read(state.upload_dir.join(&name))
        .await
        .map_err(|_| ApiError::new(404, "文件不存在"))?;
    let mut headers = HeaderMap::new();
    headers.insert(
        header::CONTENT_TYPE,
        HeaderValue::from_static("application/octet-stream"),
    );
    headers.insert(
        header::CONTENT_DISPOSITION,
        HeaderValue::from_str(&format!("attachment; filename=\"{name}\"")).unwrap(),
    );
    Ok((headers, Body::from(bytes)).into_response())
}

async fn export_resumes(State(state): State<AppState>) -> ApiResult<Response> {
    let conn = db(&state)?;
    let rows = query_values(&conn, "SELECT id,name,phone,email,highest_edu,desired_position,source,created_time FROM resume_main WHERE deleted=0 ORDER BY id", [], |r| Ok(json!([r.get::<_, i64>(0)?.to_string(), r.get::<_, Option<String>>(1)?.unwrap_or_default(), r.get::<_, Option<String>>(2)?.unwrap_or_default(), r.get::<_, Option<String>>(3)?.unwrap_or_default(), r.get::<_, Option<String>>(4)?.unwrap_or_default(), r.get::<_, Option<String>>(5)?.unwrap_or_default(), r.get::<_, Option<String>>(6)?.unwrap_or_default(), r.get::<_, String>(7)?])))?;
    let mut csv = String::from("ID,姓名,手机,邮箱,最高学历,期望职位,来源,创建时间\n");
    for r in rows {
        csv.push_str(
            &r.as_array()
                .unwrap()
                .iter()
                .map(|x| format!("\"{}\"", x.as_str().unwrap_or("").replace('"', "\"\"")))
                .collect::<Vec<_>>()
                .join(","),
        );
        csv.push('\n');
    }
    let mut headers = HeaderMap::new();
    headers.insert(
        header::CONTENT_TYPE,
        HeaderValue::from_static("text/csv; charset=utf-8"),
    );
    headers.insert(
        header::CONTENT_DISPOSITION,
        HeaderValue::from_static("attachment; filename=\"resumes.csv\""),
    );
    Ok((headers, Body::from(csv)).into_response())
}
