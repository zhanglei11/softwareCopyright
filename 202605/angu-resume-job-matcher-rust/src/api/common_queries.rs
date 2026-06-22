fn query_values<P, F, T>(conn: &Connection, sql: &str, params: P, f: F) -> ApiResult<Vec<T>>
where
    P: rusqlite::Params,
    F: FnMut(&rusqlite::Row<'_>) -> rusqlite::Result<T>,
{
    let mut stmt = conn
        .prepare(sql)
        .map_err(|e| ApiError::new(500, format!("SQL 准备失败: {e}")))?;
    let rows = stmt
        .query_map(params, f)
        .map_err(|e| ApiError::new(500, format!("SQL 查询失败: {e}")))?;
    rows.collect::<rusqlite::Result<Vec<_>>>()
        .map_err(|e| ApiError::new(500, format!("SQL 读取失败: {e}")))
}

fn list_simple<F>(
    conn: &Connection,
    table_name: &str,
    row_fn: F,
    q: &HashMap<String, String>,
    filters: &[(&str, &str)],
    base: &str,
    order: &str,
) -> ApiResult<Json<Ajax<Value>>>
where
    F: FnMut(&rusqlite::Row<'_>) -> rusqlite::Result<Value>,
{
    let mut where_sql = base.to_string();
    let mut vals: Vec<String> = vec![];
    for (param, col) in filters {
        if let Some(v) = q.get(*param).filter(|s| !s.is_empty()) {
            if *param == "status" || *param == "jobType" || *param == "eduRequire" {
                where_sql.push_str(&format!(" AND {col}=?"));
                vals.push(v.clone());
            } else {
                where_sql.push_str(&format!(" AND {col} LIKE ?"));
                vals.push(format!("%{}%", v));
            }
        }
    }
    let total: i64 = conn
        .query_row(
            &format!("SELECT COUNT(*) FROM {table_name} WHERE {where_sql}"),
            params_from_iter(vals.iter()),
            |r| r.get(0),
        )
        .unwrap_or(0);
    let (limit, offset) = page(q);
    vals.push(limit.to_string());
    vals.push(offset.to_string());
    let sql = format!("SELECT id,title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time FROM {table_name} WHERE {where_sql} ORDER BY {order} LIMIT ? OFFSET ?");
    Ok(ok(table(
        query_values(conn, &sql, params_from_iter(vals.iter()), row_fn)?,
        total,
    )))
}

fn ids_from_json(v: &Value, key: &str) -> Vec<i64> {
    v.get(key)
        .and_then(Value::as_array)
        .map(|xs| xs.iter().filter_map(Value::as_i64).collect())
        .unwrap_or_default()
}

fn role_ids_by_user(conn: &Connection, user_id: i64) -> ApiResult<Vec<i64>> {
    query_values(
        conn,
        "SELECT role_id FROM sys_user_role WHERE user_id=?",
        params![user_id],
        |r| r.get(0),
    )
}

fn replace_user_roles(conn: &Connection, user_id: i64, role_ids: Vec<i64>) -> ApiResult<()> {
    conn.execute(
        "DELETE FROM sys_user_role WHERE user_id=?",
        params![user_id],
    )
    .ok();
    for role_id in role_ids {
        conn.execute(
            "INSERT OR IGNORE INTO sys_user_role(user_id,role_id) VALUES(?,?)",
            params![user_id, role_id],
        )
        .ok();
    }
    Ok(())
}

fn replace_role_menus(conn: &Connection, role_id: i64, menu_ids: Vec<i64>) -> ApiResult<()> {
    conn.execute(
        "DELETE FROM sys_role_menu WHERE role_id=?",
        params![role_id],
    )
    .ok();
    for menu_id in menu_ids {
        conn.execute(
            "INSERT OR IGNORE INTO sys_role_menu(role_id,menu_id) VALUES(?,?)",
            params![role_id, menu_id],
        )
        .ok();
    }
    Ok(())
}

fn next_id(conn: &Connection, table: &str) -> i64 {
    conn.query_row(
        &format!("SELECT COALESCE(MAX(id),0)+1 FROM {table}"),
        [],
        |r| r.get(0),
    )
    .unwrap_or(1)
}

fn edu_level(code: &str) -> i32 {
    match code {
        "HIGH_SCHOOL" | "JUNIOR" | "SENIOR" => 1,
        "ASSOCIATE" | "COLLEGE" => 2,
        "BACHELOR" => 3,
        "MASTER" => 4,
        "DOCTOR" => 5,
        _ => 0,
    }
}

fn calc_edu_score(required: Option<&str>, actual: Option<&str>) -> f64 {
    match (required, actual) {
        (Some(r), Some(a)) => {
            let (r, a) = (edu_level(r), edu_level(a));
            if a >= r {
                100.0
            } else if a == r - 1 {
                50.0
            } else {
                0.0
            }
        }
        _ => 100.0,
    }
}

fn calc_exp_score(required: Option<i64>, actual: Option<i64>) -> f64 {
    let required = required.unwrap_or(0);
    if required <= 0 {
        return 100.0;
    }
    let actual = actual.unwrap_or(0);
    if actual <= 0 {
        0.0
    } else if actual >= required {
        100.0
    } else {
        actual as f64 * 100.0 / required as f64
    }
}

fn round2(v: f64) -> f64 {
    ((v + 1e-9) * 100.0).round() / 100.0
}
