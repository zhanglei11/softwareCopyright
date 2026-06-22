async fn get_match_config(State(state): State<AppState>) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    Ok(ok(conn.query_row("SELECT id,skill_weight,edu_weight,exp_weight,updated_time,updater_id FROM match_config WHERE id=1", [], |r| Ok(json!({"id": r.get::<_, i64>(0)?, "skillWeight": r.get::<_, i64>(1)?, "eduWeight": r.get::<_, i64>(2)?, "expWeight": r.get::<_, i64>(3)?, "updatedTime": r.get::<_, String>(4)?, "updaterId": r.get::<_, Option<i64>>(5)?}))).map_err(|_| ApiError::new(500, "匹配配置未初始化"))?))
}

async fn update_match_config(
    State(state): State<AppState>,
    headers: HeaderMap,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let skill = json_i32(&body, "skillWeight").unwrap_or(0);
    let edu = json_i32(&body, "eduWeight").unwrap_or(0);
    let exp = json_i32(&body, "expWeight").unwrap_or(0);
    if skill + edu + exp != 100 {
        return Err(ApiError::new(422, "各维度权重总和必须等于 100%"));
    }
    let conn = db(&state)?;
    conn.execute("UPDATE match_config SET skill_weight=?,edu_weight=?,exp_weight=?,updated_time=?,updater_id=? WHERE id=1", params![skill, edu, exp, now(), current_user_id(&headers)]).ok();
    Ok(ok_empty())
}

async fn run_match(
    State(state): State<AppState>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let position_id =
        json_i64(&body, "positionId").ok_or_else(|| ApiError::new(400, "职位ID不能为空"))?;
    let conn = db(&state)?;
    let job = get_job_value(&conn, position_id)?
        .ok_or_else(|| ApiError::new(400, "职位不存在或未发布"))?;
    if job["status"].as_str() != Some("OPEN") {
        return Err(ApiError::new(400, "职位不存在或未发布"));
    }
    let cfg = conn
        .query_row(
            "SELECT skill_weight,edu_weight,exp_weight FROM match_config WHERE id=1",
            [],
            |r| {
                Ok((
                    r.get::<_, f64>(0)?,
                    r.get::<_, f64>(1)?,
                    r.get::<_, f64>(2)?,
                ))
            },
        )
        .map_err(|_| ApiError::new(500, "匹配配置未初始化"))?;
    let pos_skills: Vec<String> = job["skillTags"]
        .as_array()
        .cloned()
        .unwrap_or_default()
        .into_iter()
        .filter_map(|v| v.as_str().map(|s| s.to_lowercase()))
        .collect();
    let resumes = query_values(&conn, "SELECT id,name,phone,email,gender,birth_date,city,desired_position,desired_city,desired_salary_min,desired_salary_max,job_status,highest_edu,total_exp_years,file_path,parse_success,source,self_intro,deleted,creator_id,created_time,updated_time FROM resume_main WHERE deleted=0 ORDER BY id", [], resume_row)?;
    conn.execute(
        "DELETE FROM match_result WHERE position_id=?",
        params![position_id],
    )
    .ok();
    for resume in resumes {
        let rid = resume["id"].as_i64().unwrap_or(0);
        let rskills: HashSet<String> = query_values(
            &conn,
            "SELECT skill_name FROM resume_skill WHERE resume_id=?",
            params![rid],
            |r| r.get::<_, String>(0),
        )
        .unwrap_or_default()
        .into_iter()
        .map(|s| s.to_lowercase())
        .collect();
        let skill_score = if pos_skills.is_empty() {
            100.0
        } else {
            pos_skills.iter().filter(|s| rskills.contains(*s)).count() as f64 * 100.0
                / pos_skills.len() as f64
        };
        let edu_score = calc_edu_score(job["eduRequire"].as_str(), resume["highestEdu"].as_str());
        let exp_score =
            calc_exp_score(job["expRequire"].as_i64(), resume["totalExpYears"].as_i64());
        let total = round2((skill_score * cfg.0 + edu_score * cfg.1 + exp_score * cfg.2) / 100.0);
        conn.execute("INSERT INTO match_result(position_id,resume_id,total_score,skill_score,edu_score,exp_score,matched_at) VALUES(?,?,?,?,?,?,?)",
            params![position_id, rid, total, round2(skill_score), round2(edu_score), round2(exp_score), now()]).ok();
    }
    Ok(ok(match_result_values(&conn, position_id)?))
}

async fn match_results(
    State(state): State<AppState>,
    Path(position_id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    Ok(ok(match_result_values(&conn, position_id)?))
}

fn match_result_values(conn: &Connection, position_id: i64) -> ApiResult<Value> {
    Ok(json!(query_values(conn, "SELECT mr.id,mr.position_id,mr.resume_id,mr.total_score,mr.skill_score,mr.edu_score,mr.exp_score,mr.matched_at,rm.name,rm.phone FROM match_result mr LEFT JOIN resume_main rm ON rm.id=mr.resume_id WHERE mr.position_id=? ORDER BY mr.total_score DESC", params![position_id], |r| Ok(json!({"id": r.get::<_, i64>(0)?, "positionId": r.get::<_, i64>(1)?, "resumeId": r.get::<_, i64>(2)?, "totalScore": r.get::<_, f64>(3)?, "skillScore": r.get::<_, f64>(4)?, "eduScore": r.get::<_, f64>(5)?, "expScore": r.get::<_, f64>(6)?, "matchedAt": r.get::<_, String>(7)?, "resumeName": r.get::<_, Option<String>>(8)?, "resumePhone": r.get::<_, Option<String>>(9)?})))?))
}
