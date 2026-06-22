fn get_user_by_username(conn: &Connection, username: &str) -> ApiResult<Option<Value>> {
    let mut stmt = conn.prepare("SELECT id,username,real_name,phone,password,status,error_count,locked_until,deleted,created_time,updated_time FROM sys_user WHERE username=?1 AND deleted=0")
        .map_err(|_| ApiError::new(500, "查询用户失败"))?;
    stmt.query_row(params![username], user_row)
        .optional()
        .map_err(|_| ApiError::new(500, "查询用户失败"))
}

fn get_user_by_id(conn: &Connection, id: i64) -> ApiResult<Option<Value>> {
    let mut stmt = conn.prepare("SELECT id,username,real_name,phone,password,status,error_count,locked_until,deleted,created_time,updated_time FROM sys_user WHERE id=?1 AND deleted=0")
        .map_err(|_| ApiError::new(500, "查询用户失败"))?;
    stmt.query_row(params![id], user_row)
        .optional()
        .map_err(|_| ApiError::new(500, "查询用户失败"))
}

fn user_row(row: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(json!({
        "id": row.get::<_, i64>(0)?, "username": row.get::<_, String>(1)?, "realName": row.get::<_, String>(2)?,
        "phone": row.get::<_, String>(3)?, "password": row.get::<_, String>(4)?, "status": row.get::<_, i64>(5)?,
        "errorCount": row.get::<_, i64>(6)?, "lockedUntil": row.get::<_, Option<String>>(7)?, "deleted": row.get::<_, i64>(8)?,
        "createdTime": row.get::<_, String>(9)?, "updatedTime": row.get::<_, String>(10)?
    }))
}

async fn list_users(
    State(state): State<AppState>,
    Query(q): Query<HashMap<String, String>>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let mut where_sql = " WHERE deleted=0".to_string();
    let mut values: Vec<String> = vec![];
    if let Some(v) = q.get("username").filter(|s| !s.is_empty()) {
        where_sql.push_str(" AND username LIKE ?");
        values.push(format!("%{}%", v));
    }
    if let Some(v) = q.get("phone").filter(|s| !s.is_empty()) {
        where_sql.push_str(" AND phone LIKE ?");
        values.push(format!("%{}%", v));
    }
    if let Some(v) = q.get("status").filter(|s| !s.is_empty()) {
        where_sql.push_str(" AND status=?");
        values.push(v.clone());
    }
    let total: i64 = conn
        .query_row(
            &format!("SELECT COUNT(*) FROM sys_user{}", where_sql),
            params_from_iter(values.iter()),
            |r| r.get(0),
        )
        .unwrap_or(0);
    let (limit, offset) = page(&q);
    values.push(limit.to_string());
    values.push(offset.to_string());
    let sql = format!("SELECT id,username,real_name,phone,password,status,error_count,locked_until,deleted,created_time,updated_time FROM sys_user{} ORDER BY id DESC LIMIT ? OFFSET ?", where_sql);
    let rows = query_values(&conn, &sql, params_from_iter(values.iter()), user_row)?;
    Ok(ok(table(strip_password(rows), total)))
}

fn strip_password(rows: Vec<Value>) -> Vec<Value> {
    rows.into_iter()
        .map(|mut v| {
            if let Value::Object(ref mut m) = v {
                m.remove("password");
            }
            v
        })
        .collect()
}

async fn get_user(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let mut v = get_user_by_id(&conn, id)?.ok_or_else(|| ApiError::new(404, "用户不存在"))?;
    if let Value::Object(ref mut m) = v {
        m.remove("password");
        m.insert("roleIds".into(), json!(role_ids_by_user(&conn, id)?));
    }
    Ok(ok(v))
}

async fn create_user(
    State(state): State<AppState>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let username =
        json_text(&body, "username").ok_or_else(|| ApiError::new(400, "用户名不能为空"))?;
    if get_user_by_username(&conn, &username)?.is_some() {
        return Err(ApiError::new(409, "用户名已存在"));
    }
    let real_name = json_text(&body, "realName").unwrap_or_default();
    let phone = json_text(&body, "phone").unwrap_or_default();
    let password = json_text(&body, "password").unwrap_or_else(|| "123456".into());
    let hash = bcrypt::hash(password, 10).map_err(|_| ApiError::new(500, "密码加密失败"))?;
    conn.execute("INSERT INTO sys_user(username,real_name,phone,password,status,error_count,deleted,created_time,updated_time) VALUES(?,?,?,?,1,0,0,?,?)",
        params![username, real_name, phone, hash, now(), now()]).map_err(|e| ApiError::new(409, format!("新增用户失败: {e}")))?;
    let id = conn.last_insert_rowid();
    replace_user_roles(&conn, id, ids_from_json(&body, "roleIds"))?;
    Ok(ok_empty())
}

async fn update_user(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    get_user_by_id(&conn, id)?.ok_or_else(|| ApiError::new(404, "用户不存在"))?;
    conn.execute("UPDATE sys_user SET real_name=COALESCE(?1,real_name),phone=COALESCE(?2,phone),status=COALESCE(?3,status),updated_time=?4 WHERE id=?5",
        params![json_text(&body, "realName"), json_text(&body, "phone"), json_i32(&body, "status"), now(), id]).map_err(|_| ApiError::new(500, "更新用户失败"))?;
    if body.get("roleIds").is_some() {
        replace_user_roles(&conn, id, ids_from_json(&body, "roleIds"))?;
    }
    Ok(ok_empty())
}

async fn delete_user(
    State(state): State<AppState>,
    headers: HeaderMap,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    if id == current_user_id(&headers) {
        return Err(ApiError::new(400, "不能删除当前登录账号"));
    }
    let conn = db(&state)?;
    conn.execute(
        "UPDATE sys_user SET deleted=1,updated_time=? WHERE id=?",
        params![now(), id],
    )
    .map_err(|_| ApiError::new(500, "删除用户失败"))?;
    conn.execute("DELETE FROM sys_user_role WHERE user_id=?", params![id])
        .ok();
    Ok(ok_empty())
}

async fn reset_password(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let pwd =
        json_text(&body, "newPassword").ok_or_else(|| ApiError::new(400, "新密码不能为空"))?;
    let hash = bcrypt::hash(pwd, 10).map_err(|_| ApiError::new(500, "密码加密失败"))?;
    conn.execute(
        "UPDATE sys_user SET password=?,updated_time=? WHERE id=?",
        params![hash, now(), id],
    )
    .map_err(|_| ApiError::new(500, "重置密码失败"))?;
    Ok(ok_empty())
}

async fn update_user_status(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    conn.execute(
        "UPDATE sys_user SET status=?,updated_time=? WHERE id=?",
        params![json_i32(&body, "status").unwrap_or(1), now(), id],
    )
    .map_err(|_| ApiError::new(500, "更新状态失败"))?;
    Ok(ok_empty())
}

async fn list_roles(State(state): State<AppState>) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let rows = query_values(&conn, "SELECT id,role_name,role_code,builtin,status,remark,created_time,updated_time FROM sys_role ORDER BY id", [], role_row)?;
    Ok(ok(json!(rows)))
}

fn role_row(row: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(
        json!({"id": row.get::<_, i64>(0)?, "roleName": row.get::<_, String>(1)?, "roleCode": row.get::<_, String>(2)?, "builtin": row.get::<_, i64>(3)?, "status": row.get::<_, i64>(4)?, "remark": row.get::<_, Option<String>>(5)?, "createdTime": row.get::<_, String>(6)?, "updatedTime": row.get::<_, String>(7)?}),
    )
}

async fn create_role(
    State(state): State<AppState>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    conn.execute("INSERT INTO sys_role(role_name,role_code,builtin,status,remark,created_time,updated_time) VALUES(?,?,0,1,?,?,?)",
        params![json_text(&body, "roleName").unwrap_or_default(), json_text(&body, "roleCode").unwrap_or_default(), json_text(&body, "remark"), now(), now()])
        .map_err(|_| ApiError::new(409, "角色标识已存在"))?;
    Ok(ok_empty())
}

async fn update_role(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let builtin: i64 = conn
        .query_row(
            "SELECT builtin FROM sys_role WHERE id=?",
            params![id],
            |r| r.get(0),
        )
        .optional()
        .map_err(|_| ApiError::new(500, "查询角色失败"))?
        .ok_or_else(|| ApiError::new(404, "角色不存在"))?;
    if builtin == 1 {
        return Err(ApiError::new(403, "内置角色不可编辑"));
    }
    conn.execute("UPDATE sys_role SET role_name=COALESCE(?1,role_name),role_code=COALESCE(?2,role_code),status=COALESCE(?3,status),remark=COALESCE(?4,remark),updated_time=?5 WHERE id=?6",
        params![json_text(&body, "roleName"), json_text(&body, "roleCode"), json_i32(&body, "status"), json_text(&body, "remark"), now(), id]).map_err(|_| ApiError::new(500, "更新角色失败"))?;
    Ok(ok_empty())
}

async fn delete_role(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let builtin: i64 = conn
        .query_row(
            "SELECT builtin FROM sys_role WHERE id=?",
            params![id],
            |r| r.get(0),
        )
        .optional()
        .map_err(|_| ApiError::new(500, "查询角色失败"))?
        .ok_or_else(|| ApiError::new(404, "角色不存在"))?;
    if builtin == 1 {
        return Err(ApiError::new(403, "内置角色不可删除"));
    }
    let count: i64 = conn
        .query_row(
            "SELECT COUNT(*) FROM sys_user_role WHERE role_id=?",
            params![id],
            |r| r.get(0),
        )
        .unwrap_or(0);
    if count > 0 {
        return Err(ApiError::new(
            409,
            format!("该角色下有 {count} 个用户，请先解绑用户"),
        ));
    }
    conn.execute("DELETE FROM sys_role WHERE id=?", params![id])
        .ok();
    conn.execute("DELETE FROM sys_role_menu WHERE role_id=?", params![id])
        .ok();
    Ok(ok_empty())
}

async fn assign_role_menus(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    replace_role_menus(&conn, id, ids_from_json(&body, "menuIds"))?;
    Ok(ok_empty())
}

async fn menu_tree(State(state): State<AppState>) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let menus = query_values(&conn, "SELECT id,parent_id,menu_type,menu_name,path,perm_code,icon,sort FROM sys_menu ORDER BY sort,id", [], menu_row)?;
    Ok(ok(json!(build_tree(menus, 0))))
}

fn menu_row(row: &rusqlite::Row) -> rusqlite::Result<Value> {
    Ok(
        json!({"id": row.get::<_, i64>(0)?, "parentId": row.get::<_, i64>(1)?, "menuType": row.get::<_, i64>(2)?, "menuName": row.get::<_, String>(3)?, "path": row.get::<_, Option<String>>(4)?, "permCode": row.get::<_, Option<String>>(5)?, "icon": row.get::<_, Option<String>>(6)?, "sort": row.get::<_, i64>(7)?, "children": []}),
    )
}

fn build_tree(all: Vec<Value>, parent_id: i64) -> Vec<Value> {
    let mut result = Vec::new();
    for item in all
        .iter()
        .filter(|m| m["parentId"].as_i64() == Some(parent_id))
    {
        let mut node = item.clone();
        if let Value::Object(ref mut obj) = node {
            obj.insert(
                "children".into(),
                Value::Array(build_tree(all.clone(), item["id"].as_i64().unwrap_or(0))),
            );
        }
        result.push(node);
    }
    result
}

async fn create_menu(
    State(state): State<AppState>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let id = json_i64(&body, "id").unwrap_or_else(|| next_id(&conn, "sys_menu"));
    conn.execute("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,path,perm_code,icon,sort) VALUES(?,?,?,?,?,?,?,?)",
        params![id, json_i64(&body, "parentId").unwrap_or(0), json_i32(&body, "menuType").unwrap_or(1), json_text(&body, "menuName").unwrap_or_default(), json_text(&body, "path"), json_text(&body, "permCode"), json_text(&body, "icon"), json_i32(&body, "sort").unwrap_or(0)])
        .map_err(|_| ApiError::new(500, "新增菜单失败"))?;
    Ok(ok_empty())
}

async fn update_menu(
    State(state): State<AppState>,
    Path(id): Path<i64>,
    Json(body): Json<Value>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    conn.execute("UPDATE sys_menu SET parent_id=COALESCE(?1,parent_id),menu_type=COALESCE(?2,menu_type),menu_name=COALESCE(?3,menu_name),path=?4,perm_code=?5,icon=?6,sort=COALESCE(?7,sort) WHERE id=?8",
        params![json_i64(&body, "parentId"), json_i32(&body, "menuType"), json_text(&body, "menuName"), json_text(&body, "path"), json_text(&body, "permCode"), json_text(&body, "icon"), json_i32(&body, "sort"), id])
        .map_err(|_| ApiError::new(500, "更新菜单失败"))?;
    Ok(ok_empty())
}

async fn delete_menu(
    State(state): State<AppState>,
    Path(id): Path<i64>,
) -> ApiResult<Json<Ajax<Value>>> {
    let conn = db(&state)?;
    let children: i64 = conn
        .query_row(
            "SELECT COUNT(*) FROM sys_menu WHERE parent_id=?",
            params![id],
            |r| r.get(0),
        )
        .unwrap_or(0);
    if children > 0 {
        return Err(ApiError::new(400, "请先删除子菜单"));
    }
    conn.execute("DELETE FROM sys_menu WHERE id=?", params![id])
        .ok();
    conn.execute("DELETE FROM sys_role_menu WHERE menu_id=?", params![id])
        .ok();
    Ok(ok_empty())
}
