#![recursion_limit = "512"]

use std::{
    collections::{HashMap, HashSet},
    fs,
    net::SocketAddr,
    path::PathBuf,
    sync::{Arc, Mutex},
};

use anyhow::{anyhow, Context, Result};
use axum::{
    body::Body,
    extract::{Multipart, Path, Query, State},
    http::{header, HeaderMap, HeaderValue, Method, StatusCode},
    response::{IntoResponse, Response},
    routing::{get, post, put},
    Json, Router,
};
use bcrypt::verify;
use chrono::{Duration, Local, Utc};
use jsonwebtoken::{decode, encode, DecodingKey, EncodingKey, Header, Validation};
use rusqlite::{params, params_from_iter, Connection, OptionalExtension};
use serde::{Deserialize, Serialize};
use serde_json::{json, Value};
use tower_http::{
    cors::{Any, CorsLayer},
    trace::TraceLayer,
};
use uuid::Uuid;

const JWT_SECRET: &[u8] = b"AnguResumeJobMatcher@SecretKey2026!";

#[derive(Clone)]
struct AppState {
    db: Arc<Mutex<Connection>>,
    upload_dir: PathBuf,
}

#[derive(Debug)]
struct ApiError {
    code: i32,
    message: String,
}

impl ApiError {
    fn new(code: i32, message: impl Into<String>) -> Self {
        Self {
            code,
            message: message.into(),
        }
    }
}

impl IntoResponse for ApiError {
    fn into_response(self) -> Response {
        let status = match self.code {
            400 => StatusCode::BAD_REQUEST,
            401 => StatusCode::OK,
            403 => StatusCode::FORBIDDEN,
            404 => StatusCode::OK,
            409 => StatusCode::OK,
            422 => StatusCode::OK,
            _ => StatusCode::OK,
        };
        (
            status,
            Json(json!({"code": self.code, "message": self.message, "data": Value::Null})),
        )
            .into_response()
    }
}

type ApiResult<T> = std::result::Result<T, ApiError>;

#[derive(Serialize)]
struct Ajax<T: Serialize> {
    code: i32,
    message: &'static str,
    data: T,
}

fn ok<T: Serialize>(data: T) -> Json<Ajax<T>> {
    Json(Ajax {
        code: 200,
        message: "操作成功",
        data,
    })
}

fn ok_empty() -> Json<Ajax<Value>> {
    ok(Value::Null)
}

#[derive(Debug, Serialize, Deserialize)]
struct Claims {
    sub: String,
    user_id: i64,
    #[serde(rename = "type")]
    token_type: String,
    exp: usize,
}

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
struct LoginReq {
    username: String,
    password: String,
}

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
struct RefreshReq {
    refresh_token: String,
}

#[tokio::main]
async fn main() -> Result<()> {
    tracing_subscriber::fmt()
        .with_env_filter(
            tracing_subscriber::EnvFilter::from_default_env().add_directive("info".parse()?),
        )
        .init();

    let root = std::env::current_dir().context("读取当前目录失败")?;
    let db_path = std::env::var("DATABASE_PATH").unwrap_or_else(|_| {
        root.join("angu_resume_job_matcher.sqlite")
            .display()
            .to_string()
    });
    let upload_dir = PathBuf::from(
        std::env::var("UPLOAD_DIR").unwrap_or_else(|_| root.join("uploads").display().to_string()),
    );
    fs::create_dir_all(&upload_dir).context("创建上传目录失败")?;

    let conn = Connection::open(db_path).context("打开 SQLite 数据库失败")?;
    init_db(&conn).context("初始化数据库失败")?;

    let state = AppState {
        db: Arc::new(Mutex::new(conn)),
        upload_dir,
    };
    let app = Router::new()
        .route("/v3/api-docs", get(openapi_json))
        .route("/swagger-ui.html", get(swagger_ui))
        .route("/swagger-ui", get(swagger_ui))
        .route("/api/auth/login", post(login))
        .route("/api/auth/refresh", post(refresh))
        .route("/api/auth/logout", post(|| async { ok_empty() }))
        .route("/api/auth/me", get(me))
        .route("/api/system/menus/tree", get(menu_tree))
        .route("/api/system/menus/my-tree", get(menu_tree))
        .route("/api/system/menus", post(create_menu))
        .route(
            "/api/system/menus/:id",
            put(update_menu).delete(delete_menu),
        )
        .route("/api/system/roles", get(list_roles).post(create_role))
        .route(
            "/api/system/roles/:id",
            put(update_role).delete(delete_role),
        )
        .route("/api/system/roles/:id/menus", put(assign_role_menus))
        .route("/api/system/users", get(list_users).post(create_user))
        .route(
            "/api/system/users/:id",
            get(get_user).put(update_user).delete(delete_user),
        )
        .route("/api/system/users/:id/reset-password", put(reset_password))
        .route("/api/system/users/:id/status", put(update_user_status))
        .route("/api/jobs", get(list_jobs).post(create_job))
        .route(
            "/api/jobs/:id",
            get(get_job).put(update_job).delete(delete_job),
        )
        .route("/api/jobs/:id/publish", put(publish_job))
        .route("/api/jobs/:id/close", put(close_job))
        .route("/api/resumes", get(list_resumes).post(create_resume))
        .route("/api/resumes/upload", post(upload_resume))
        .route("/api/resumes/export", get(export_resumes))
        .route(
            "/api/resumes/:id",
            get(get_resume).put(update_resume).delete(delete_resume),
        )
        .route("/api/resumes/:id/file", get(download_resume_file))
        .route("/api/match/run", post(run_match))
        .route("/api/match/results/:position_id", get(match_results))
        .route(
            "/api/match/config",
            get(get_match_config).put(update_match_config),
        )
        .route(
            "/api/applications",
            get(list_applications).post(create_application),
        )
        .route("/api/applications/:id", get(get_application))
        .route(
            "/api/applications/:id/status",
            put(update_application_status),
        )
        .route("/api/applications/:id/logs", get(application_logs))
        .route(
            "/api/interviews",
            get(list_interviews).post(create_interview),
        )
        .route(
            "/api/interviews/:id",
            get(get_interview).put(update_interview),
        )
        .route("/api/interviews/:id/result", put(update_interview_result))
        .route("/api/stats/dashboard", get(stats_dashboard))
        .route("/api/stats/source", get(stats_source))
        .with_state(state)
        .layer(TraceLayer::new_for_http())
        .layer(
            CorsLayer::new()
                .allow_origin(Any)
                .allow_methods([
                    Method::GET,
                    Method::POST,
                    Method::PUT,
                    Method::DELETE,
                    Method::OPTIONS,
                ])
                .allow_headers(Any),
        );

    let addr: SocketAddr = "127.0.0.1:19915".parse()?;
    tracing::info!("Rust backend listening on http://{}", addr);
    let listener = tokio::net::TcpListener::bind(addr).await?;
    axum::serve(listener, app).await?;
    Ok(())
}

include!("api/support.rs");
include!("api/auth.rs");
include!("api/system.rs");
include!("api/jobs.rs");
include!("api/resumes.rs");
include!("api/matching.rs");
include!("api/applications.rs");
include!("api/interviews.rs");
include!("api/stats.rs");
include!("api/common_queries.rs");
include!("db_init.rs");
include!("swagger.rs");
