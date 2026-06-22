async fn stats_dashboard(State(state): State<AppState>) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let ym = Local::now().format("%Y-%m").to_string();
    let count = |sql: &str, val: &str| -> i64 {
        conn.query_row(sql, params![val], |r| r.get(0)).unwrap_or(0)
    };
    let applied = count(
        "SELECT COUNT(*) FROM job_application WHERE substr(created_time,1,7)=?",
        &ym,
    );
    let interviewed = count("SELECT COUNT(*) FROM interview_record WHERE result IS NOT NULL AND substr(created_time,1,7)=?", &ym);
    let hired = count(
        "SELECT COUNT(*) FROM job_application WHERE status='HIRED' AND substr(operate_time,1,7)=?",
        &ym,
    );
    let passed = count("SELECT COUNT(*) FROM job_application WHERE status NOT IN('PENDING','RESUME_REJECTED') AND substr(created_time,1,7)=?", &ym);
    let interviewing = count("SELECT COUNT(*) FROM job_application WHERE status IN('INTERVIEWING','INTERVIEW_PASSED','INTERVIEW_REJECTED','HIRED') AND substr(created_time,1,7)=?", &ym);
    Ok(ok(
        json!({"monthlyApplications": applied, "monthlyInterviewDone": interviewed, "monthlyHired": hired, "funnel": {"applied": applied, "passed": passed, "interviewed": interviewing, "hired": hired}}),
    ))
}

async fn stats_source(
    State(state): State<AppState>,
    Query(q): Query<HashMap<String, String>>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let mut sql =
        "SELECT COALESCE(source,'UNKNOWN'),COUNT(*) FROM resume_main WHERE deleted=0".to_string();
    let mut vals = vec![];
    if let Some(v) = q.get("startDate").filter(|s| !s.is_empty()) {
        sql.push_str(" AND date(created_time)>=date(?)");
        vals.push(v.clone());
    }
    if let Some(v) = q.get("endDate").filter(|s| !s.is_empty()) {
        sql.push_str(" AND date(created_time)<=date(?)");
        vals.push(v.clone());
    }
    sql.push_str(" GROUP BY source");
    let rows = query_values(&conn, &sql, params_from_iter(vals.iter()), |r| {
        Ok(json!({"source": r.get::<_, String>(0)?, "cnt": r.get::<_, i64>(1)?}))
    })?;
    Ok(ok(json!({"distribution": rows})))
}
