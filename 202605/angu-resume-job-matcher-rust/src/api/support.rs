fn db(state: &AppState) -> ApiResult<std::sync::MutexGuard<'_, Connection>> {
    state
        .db
        .lock()
        .map_err(|_| ApiError::new(500, "数据库连接锁定失败"))
}

fn now() -> String {
    Local::now().format("%Y-%m-%d %H:%M:%S").to_string()
}

fn json_text(v: &Value, key: &str) -> Option<String> {
    v.get(key).and_then(Value::as_str).map(str::to_string)
}

fn json_i64(v: &Value, key: &str) -> Option<i64> {
    v.get(key).and_then(Value::as_i64)
}

fn json_i32(v: &Value, key: &str) -> Option<i32> {
    json_i64(v, key).map(|x| x as i32)
}

fn page(params: &HashMap<String, String>) -> (i64, i64) {
    let p = params
        .get("page")
        .and_then(|x| x.parse::<i64>().ok())
        .unwrap_or(1)
        .max(1);
    let s = params
        .get("size")
        .and_then(|x| x.parse::<i64>().ok())
        .unwrap_or(20)
        .clamp(1, 200);
    (s, (p - 1) * s)
}

fn table(rows: Vec<Value>, total: i64) -> Value {
    json!({ "rows": rows, "total": total })
}

fn create_token(
    username: &str,
    user_id: i64,
    token_type: &str,
    ttl: Duration,
) -> ApiResult<String> {
    let exp = (Utc::now() + ttl).timestamp() as usize;
    encode(
        &Header::default(),
        &Claims {
            sub: username.to_string(),
            user_id,
            token_type: token_type.to_string(),
            exp,
        },
        &EncodingKey::from_secret(JWT_SECRET),
    )
    .map_err(|_| ApiError::new(500, "Token 生成失败"))
}

fn current_user_id(headers: &HeaderMap) -> i64 {
    headers
        .get(header::AUTHORIZATION)
        .and_then(|v| v.to_str().ok())
        .and_then(|v| v.strip_prefix("Bearer "))
        .and_then(|token| {
            decode::<Claims>(
                token,
                &DecodingKey::from_secret(JWT_SECRET),
                &Validation::default(),
            )
            .ok()
        })
        .map(|data| data.claims.user_id)
        .unwrap_or(1)
}
