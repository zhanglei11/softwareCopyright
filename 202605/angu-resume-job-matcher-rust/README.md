# Angu Resume Job Matcher Rust

这是 `angu-resume-job-matcher` Java 后端的 Rust 复刻版本，前端仍使用原来的 `angu-resume-job-matcher-web`，接口路径保持 `http://127.0.0.1:19915/api/...`。

## 技术栈

- Rust 2021
- Axum HTTP 服务
- SQLite 本地数据库
- Rusqlite 数据访问
- JWT 登录态

## 启动

```bash
cd /Users/zhanglei/cz/project/softwareCopyright/202605/angu-resume-job-matcher-rust
/Users/zhanglei/.cargo/bin/cargo run
```

默认监听：

```text
http://127.0.0.1:19915
```

Swagger 文档：

```text
http://127.0.0.1:19915/swagger-ui.html
http://127.0.0.1:19915/v3/api-docs
```

默认数据库文件：

```text
angu_resume_job_matcher.sqlite
```

可用环境变量：

```bash
DATABASE_PATH=/tmp/angu_resume_job_matcher.sqlite /Users/zhanglei/.cargo/bin/cargo run
UPLOAD_DIR=/tmp/angu_uploads /Users/zhanglei/.cargo/bin/cargo run
```

## 默认账号

```text
username: admin
password: admin123
```

同时兼容 `123456`，并保留原数据库 dump 中的 bcrypt 密码校验。

## 已实现业务

- 登录、刷新 Token、退出、当前用户
- 菜单树、用户、角色、角色菜单授权
- 职位列表、新增、编辑、删除、发布、关闭
- 简历列表、详情、新增、编辑、删除、上传、下载、导出 CSV
- 智能匹配、匹配结果、匹配权重配置
- 投递列表、详情、创建、状态流转、操作日志
- 面试列表、详情、创建、编辑、填写结果并推进投递状态
- 数据看板、简历来源统计

## 代码结构

```text
src/main.rs                 # 应用启动、路由注册、CORS/Trace 中间件
src/swagger.rs              # OpenAPI JSON 与 Swagger UI 页面
src/db_init.rs              # SQLite 表结构与初始化种子数据
src/api/support.rs          # 通用响应、分页、JSON 取值、JWT 用户解析
src/api/common_queries.rs   # 通用查询、角色/菜单关系、匹配评分辅助函数
src/api/auth.rs             # 登录、刷新 Token、当前用户
src/api/system.rs           # 用户、角色、菜单管理
src/api/jobs.rs             # 职位管理
src/api/resumes.rs          # 简历管理、上传、下载、导出
src/api/matching.rs         # 智能匹配与匹配配置
src/api/applications.rs     # 投递记录、状态流转、操作日志
src/api/interviews.rs       # 面试安排与面试结果
src/api/stats.rs            # 数据看板和来源统计
```
