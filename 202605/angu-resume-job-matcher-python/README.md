# Angu Resume Job Matcher Python

这是 `angu-resume-job-matcher` 的 Python 标准库重写版本，接口路径兼容原 Vue 前端，不修改原 Java 项目和前端项目。

## 启动

```bash
cd /Users/zhanglei/cz/project/softwareCopyright/202605/angu-resume-job-matcher-python
python3 run.py
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

默认优先连接同级 Rust 项目的 SQLite 数据库：

```text
/Users/zhanglei/cz/project/softwareCopyright/202605/angu-resume-job-matcher-rust/angu_resume_job_matcher.sqlite
```

如果不存在，则在本项目下创建：

```text
angu_resume_job_matcher.sqlite
```

也可以显式指定：

```bash
DATABASE_PATH=/path/to/angu_resume_job_matcher.sqlite python3 run.py
```

## 默认账号

```text
username: admin
password: admin123
```

## 代码结构

```text
run.py              # 启动入口
app/server.py      # HTTP 服务与路由分发
app/services.py    # 业务逻辑
app/db.py          # SQLite 连接、建表、初始化数据
app/common.py      # 响应包装、JWT、分页、字段转换
app/swagger.py     # OpenAPI/Swagger 文档
```

## 已实现接口

- 认证：登录、刷新、退出、当前用户
- 系统：菜单、用户、角色、角色菜单授权
- 职位：列表、详情、新增、编辑、发布、关闭、删除
- 简历：列表、详情、新增、编辑、删除、上传、下载、导出
- 匹配：执行匹配、结果、配置
- 投递：列表、详情、创建、状态流转、日志
- 面试：列表、详情、创建、编辑、填写结果
- 统计：数据看板、来源统计
