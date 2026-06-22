# Angu Resume Job Matcher Go

这是 `angu-resume-job-matcher` 的 Go 重写版本，接口路径兼容原 Vue 前端，不修改原 Java 项目和前端项目。

## 启动

```bash
cd /Users/zhanglei/cz/project/softwareCopyright/202605/angu-resume-job-matcher-go
go mod tidy
go run .
```

默认监听：

```text
http://127.0.0.1:19915
```

Swagger：

```text
http://127.0.0.1:19915/swagger-ui.html
http://127.0.0.1:19915/v3/api-docs
```

## 数据库

默认优先连接本地已有 SQLite：

```text
/Users/zhanglei/cz/project/softwareCopyright/202605/angu-resume-job-matcher-rust/angu_resume_job_matcher.sqlite
```

如果不存在，则在 Go 项目目录下创建：

```text
angu_resume_job_matcher.sqlite
```

也可以显式指定：

```bash
DATABASE_PATH=/path/to/angu_resume_job_matcher.sqlite go run .
```

## 默认账号

```text
username: admin
password: admin123
```

## 代码结构

```text
main.go                 # HTTP 服务与路由注册
internal/api/common.go  # 统一响应、JWT、分页、JSON 工具
internal/db/db.go       # SQLite 连接、建表、初始化数据、字段转换
internal/service        # 业务逻辑
internal/swagger        # OpenAPI JSON 与 Swagger UI
```

## 依赖

使用纯 Go SQLite 驱动：

```text
modernc.org/sqlite
```

首次运行需要 Go 工具链下载依赖。
