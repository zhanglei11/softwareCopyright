from typing import Any


def prop(t: str, desc: str, example: Any = None, **extra) -> dict[str, Any]:
    data = {"type": t, "description": desc}
    if example is not None:
        data["example"] = example
    data.update(extra)
    return data


def ref(name: str) -> dict[str, Any]:
    return {"$ref": f"#/components/schemas/{name}"}


def arr(name: str) -> dict[str, Any]:
    return {"type": "array", "items": ref(name)}


def ajax(data_schema: dict[str, Any] | None = None) -> dict[str, Any]:
    schema = {"$ref": "#/components/schemas/AjaxResult"} if data_schema is None else {
        "allOf": [
            {"$ref": "#/components/schemas/AjaxResult"},
            {"type": "object", "properties": {"data": data_schema}},
        ]
    }
    return {"description": "成功", "content": {"application/json": {"schema": schema}}}


def page_of(name: str) -> dict[str, Any]:
    return {"allOf": [{"$ref": "#/components/schemas/PageResult"}, {"type": "object", "properties": {"rows": arr(name)}}]}


def body(name: str) -> dict[str, Any]:
    return {"required": True, "content": {"application/json": {"schema": ref(name)}}}


def pid(name: str = "id") -> dict[str, Any]:
    return {"name": name, "in": "path", "required": True, "description": "资源 ID", "schema": {"type": "integer"}}


def q(name: str, desc: str, t: str = "string") -> dict[str, Any]:
    return {"name": name, "in": "query", "description": desc, "schema": {"type": t}}


def page_params(*extra):
    return [q("page", "页码，从 1 开始", "integer"), q("size", "每页条数", "integer"), *extra]


def schemas() -> dict[str, Any]:
    return {
        "AjaxResult": {"type": "object", "description": "统一响应包装，成功 code=200，失败返回业务错误码。", "properties": {"code": prop("integer", "业务状态码", 200), "message": prop("string", "响应消息", "操作成功"), "data": {"description": "业务数据，无数据时为 null"}}},
        "PageResult": {"type": "object", "description": "分页响应结构。", "properties": {"rows": {"type": "array", "description": "当前页记录", "items": {"type": "object"}}, "total": prop("integer", "总记录数", 12)}},
        "LoginRequest": {"type": "object", "description": "登录请求", "required": ["username", "password"], "properties": {"username": prop("string", "用户名，不能为空", "admin"), "password": prop("string", "密码，不能为空", "admin123")}},
        "LoginResponse": {"type": "object", "description": "登录响应", "properties": {"accessToken": prop("string", "访问令牌，前端放入 Authorization 请求头"), "refreshToken": prop("string", "刷新令牌"), "userId": prop("integer", "用户 ID", 1), "username": prop("string", "用户名", "admin"), "realName": prop("string", "真实姓名", "System Admin")}},
        "RefreshTokenRequest": {"type": "object", "description": "刷新 Token 请求", "required": ["refreshToken"], "properties": {"refreshToken": prop("string", "刷新令牌")}},
        "SysUser": {"type": "object", "description": "系统用户，响应中不包含密码。", "properties": {"id": prop("integer", "用户 ID", 1), "username": prop("string", "用户名", "admin"), "realName": prop("string", "真实姓名", "System Admin"), "phone": prop("string", "手机号", "13800000000"), "status": prop("integer", "状态：1 启用，0 禁用", 1), "createdTime": prop("string", "创建时间", "2026-05-27 14:37:46"), "updatedTime": prop("string", "更新时间", "2026-05-27 14:37:46"), "roleIds": {"type": "array", "description": "角色 ID 列表", "items": {"type": "integer"}}}},
        "UserCreateRequest": {"type": "object", "description": "新增用户请求", "required": ["username", "realName", "phone", "password"], "properties": {"username": prop("string", "用户名，唯一", "hr01"), "realName": prop("string", "真实姓名", "招聘专员"), "phone": prop("string", "手机号，唯一", "13800000001"), "password": prop("string", "初始密码", "Admin@123"), "roleIds": {"type": "array", "description": "角色 ID 列表", "items": {"type": "integer"}}}},
        "UserUpdateRequest": {"type": "object", "description": "编辑用户请求", "properties": {"realName": prop("string", "真实姓名"), "phone": prop("string", "手机号"), "status": prop("integer", "状态：1 启用，0 禁用"), "roleIds": {"type": "array", "description": "角色 ID 列表", "items": {"type": "integer"}}}},
        "ResetPasswordRequest": {"type": "object", "description": "重置密码请求", "properties": {"newPassword": prop("string", "新密码", "Admin@123")}},
        "UserStatusRequest": {"type": "object", "description": "启用/禁用用户请求", "properties": {"status": prop("integer", "状态：1 启用，0 禁用", 0)}},
        "SysRole": {"type": "object", "description": "系统角色", "properties": {"id": prop("integer", "角色 ID", 1), "roleName": prop("string", "角色名称", "Super Admin"), "roleCode": prop("string", "角色标识", "SUPER_ADMIN"), "builtin": prop("integer", "是否内置：1 是，0 否", 1), "status": prop("integer", "状态：1 启用，0 禁用", 1), "remark": prop("string", "备注")}},
        "SysRoleRequest": {"type": "object", "description": "新增/编辑角色请求", "properties": {"roleName": prop("string", "角色名称", "HR Admin"), "roleCode": prop("string", "角色标识", "HR_ADMIN"), "status": prop("integer", "状态", 1), "remark": prop("string", "备注")}},
        "RoleMenuRequest": {"type": "object", "description": "角色菜单授权请求", "properties": {"menuIds": {"type": "array", "description": "菜单/按钮 ID 列表", "items": {"type": "integer"}}}},
        "SysMenu": {"type": "object", "description": "系统菜单树节点", "properties": {"id": prop("integer", "菜单 ID", 1), "parentId": prop("integer", "父菜单 ID，根节点为 0", 0), "menuType": prop("integer", "类型：0 目录，1 菜单，2 按钮", 0), "menuName": prop("string", "菜单名称", "System"), "path": prop("string", "前端路由", "/system"), "permCode": prop("string", "权限标识"), "icon": prop("string", "图标标识"), "sort": prop("integer", "排序值", 1), "children": {"type": "array", "description": "子节点", "items": ref("SysMenu")}}},
        "SysMenuRequest": {"type": "object", "description": "新增/编辑菜单请求", "properties": {"parentId": prop("integer", "父菜单 ID", 0), "menuType": prop("integer", "类型：0/1/2", 1), "menuName": prop("string", "菜单名称"), "path": prop("string", "路由路径"), "permCode": prop("string", "权限标识"), "icon": prop("string", "图标"), "sort": prop("integer", "排序")}},
        "JobPositionRequest": {"type": "object", "description": "职位创建/编辑请求", "required": ["title"], "properties": {"title": prop("string", "职位名称，不能为空", "Rust 后端工程师"), "department": prop("string", "所属部门", "智能招聘部"), "jobType": prop("string", "岗位类型：FULL_TIME/PART_TIME/INTERN", "FULL_TIME"), "location": prop("string", "工作地点", "杭州"), "salaryMin": prop("integer", "薪资下限", 15000), "salaryMax": prop("integer", "薪资上限", 25000), "eduRequire": prop("string", "学历要求：HIGH_SCHOOL/ASSOCIATE/BACHELOR/MASTER/DOCTOR", "BACHELOR"), "expRequire": prop("integer", "经验年限要求", 3), "description": prop("string", "职位描述"), "skillTags": {"type": "array", "description": "技能标签", "items": {"type": "string"}}}},
        "JobPosition": {"allOf": [ref("JobPositionRequest"), {"type": "object", "description": "职位信息响应", "properties": {"id": prop("integer", "职位 ID", 1), "status": prop("string", "状态：DRAFT/OPEN/CLOSED", "OPEN"), "creatorId": prop("integer", "创建人 ID", 1), "createdTime": prop("string", "创建时间"), "updatedTime": prop("string", "更新时间")}}]},
        "ResumeEducation": {"type": "object", "description": "教育经历", "properties": {"school": prop("string", "学校"), "major": prop("string", "专业"), "eduLevel": prop("string", "学历等级"), "startDate": prop("string", "开始日期"), "endDate": prop("string", "结束日期")}},
        "ResumeWorkExp": {"type": "object", "description": "工作经历", "properties": {"company": prop("string", "公司"), "position": prop("string", "职位"), "industry": prop("string", "行业"), "startDate": prop("string", "开始日期"), "endDate": prop("string", "结束日期"), "description": prop("string", "工作内容")}},
        "ResumeRequest": {"type": "object", "description": "简历创建/编辑请求", "properties": {"name": prop("string", "候选人姓名"), "phone": prop("string", "手机号"), "email": prop("string", "邮箱"), "gender": prop("integer", "性别"), "birthDate": prop("string", "出生日期 yyyy-MM-dd"), "city": prop("string", "当前城市"), "desiredPosition": prop("string", "期望职位"), "desiredCity": prop("string", "期望城市"), "source": prop("string", "简历来源，默认 MANUAL"), "selfIntro": prop("string", "个人简介"), "skills": {"type": "array", "description": "技能名称列表", "items": {"type": "string"}}, "educations": {"type": "array", "description": "教育经历", "items": ref("ResumeEducation")}, "workExps": {"type": "array", "description": "工作经历", "items": ref("ResumeWorkExp")}}},
        "ResumeMain": {"allOf": [ref("ResumeRequest"), {"type": "object", "description": "简历响应", "properties": {"id": prop("integer", "简历 ID"), "highestEdu": prop("string", "最高学历"), "totalExpYears": prop("integer", "总经验年限"), "filePath": prop("string", "附件文件名"), "skills": {"type": "array", "description": "技能对象列表", "items": {"type": "object"}}}}]},
        "MatchConfigRequest": {"type": "object", "description": "匹配规则配置，三个权重之和必须为 100。", "properties": {"skillWeight": prop("integer", "技能权重", 50), "eduWeight": prop("integer", "学历权重", 30), "expWeight": prop("integer", "经验权重", 20)}},
        "MatchConfig": {"allOf": [ref("MatchConfigRequest"), {"type": "object", "properties": {"id": prop("integer", "配置 ID", 1), "updatedTime": prop("string", "更新时间"), "updaterId": prop("integer", "更新人 ID")}}]},
        "RunMatchRequest": {"type": "object", "description": "发起匹配请求", "properties": {"positionId": prop("integer", "职位 ID，职位必须为 OPEN", 1)}},
        "MatchResult": {"type": "object", "description": "匹配结果", "properties": {"id": prop("integer", "结果 ID"), "positionId": prop("integer", "职位 ID"), "resumeId": prop("integer", "简历 ID"), "totalScore": prop("number", "综合分", 63.33), "skillScore": prop("number", "技能分"), "eduScore": prop("number", "学历分"), "expScore": prop("number", "经验分"), "resumeName": prop("string", "候选人姓名"), "resumePhone": prop("string", "手机号")}},
        "ApplicationCreateRequest": {"type": "object", "description": "创建投递记录请求", "properties": {"positionId": prop("integer", "职位 ID，必须 OPEN"), "resumeId": prop("integer", "简历 ID"), "remark": prop("string", "备注")}},
        "ApplicationStatusRequest": {"type": "object", "description": "变更投递状态请求，必须遵循合法招聘流转。", "properties": {"status": prop("string", "目标状态", "RESUME_PASSED"), "remark": prop("string", "备注")}},
        "JobApplication": {"type": "object", "description": "投递记录", "properties": {"id": prop("integer", "投递 ID"), "positionId": prop("integer", "职位 ID"), "resumeId": prop("integer", "简历 ID"), "status": prop("string", "投递状态"), "positionTitle": prop("string", "职位名称"), "resumeName": prop("string", "候选人姓名")}},
        "ApplicationLog": {"type": "object", "description": "投递操作日志", "properties": {"fromStatus": prop("string", "原状态"), "toStatus": prop("string", "目标状态"), "operatorName": prop("string", "操作人"), "remark": prop("string", "备注"), "createdTime": prop("string", "操作时间")}},
        "InterviewRequest": {"type": "object", "description": "新增/编辑面试请求", "properties": {"applicationId": prop("integer", "投递 ID"), "interviewTime": prop("string", "面试时间 yyyy-MM-dd HH:mm:ss"), "interviewer": prop("string", "面试官"), "location": prop("string", "面试地点")}},
        "InterviewResultRequest": {"type": "object", "description": "填写面试结果，PASS 会推进到 INTERVIEW_PASSED，否则 INTERVIEW_REJECTED。", "properties": {"score": prop("integer", "评分 1-5"), "comment": prop("string", "评价"), "result": prop("string", "结果 PASS/REJECT", "PASS")}},
        "InterviewRecord": {"type": "object", "description": "面试记录", "properties": {"id": prop("integer", "面试 ID"), "applicationId": prop("integer", "投递 ID"), "interviewTime": prop("string", "面试时间"), "interviewer": prop("string", "面试官"), "location": prop("string", "地点"), "score": prop("integer", "评分"), "comment": prop("string", "评价"), "result": prop("string", "结果")}},
        "DashboardStats": {"type": "object", "description": "数据看板统计", "properties": {"monthlyApplications": prop("integer", "本月投递数"), "monthlyInterviewDone": prop("integer", "本月已完成面试数"), "monthlyHired": prop("integer", "本月录用数"), "funnel": {"type": "object", "description": "招聘漏斗"}}},
        "SourceStats": {"type": "object", "description": "简历来源统计", "properties": {"distribution": {"type": "array", "description": "来源分布", "items": {"type": "object"}}}},
    }


def op(tag, summary, data=None, params=None, req=None):
    item = {"tags": [tag], "summary": summary, "responses": {"200": ajax(data)}}
    if params:
        item["parameters"] = params
    if req:
        item["requestBody"] = body(req)
    return item


def openapi() -> dict[str, Any]:
    return {
        "openapi": "3.0.3",
        "info": {"title": "Angu Resume Job Matcher Python API", "version": "0.1.0", "description": "Python 标准库重写版本，接口兼容原 Vue 前端。"},
        "servers": [{"url": "http://127.0.0.1:19915"}],
        "components": {"securitySchemes": {"bearerAuth": {"type": "http", "scheme": "bearer", "bearerFormat": "JWT"}}, "schemas": schemas()},
        "security": [{"bearerAuth": []}],
        "paths": {
            "/api/auth/login": {"post": op("认证管理", "登录", ref("LoginResponse"), req="LoginRequest")},
            "/api/auth/refresh": {"post": op("认证管理", "刷新 Token", ref("LoginResponse"), req="RefreshTokenRequest")},
            "/api/auth/logout": {"post": op("认证管理", "退出登录")},
            "/api/auth/me": {"get": op("认证管理", "当前用户", ref("SysUser"))},
            "/api/system/menus/tree": {"get": op("系统菜单", "菜单树", arr("SysMenu"))},
            "/api/system/menus/my-tree": {"get": op("系统菜单", "当前用户菜单树", arr("SysMenu"))},
            "/api/system/menus": {"post": op("系统菜单", "新增菜单", req="SysMenuRequest")},
            "/api/system/menus/{id}": {"put": op("系统菜单", "编辑菜单", params=[pid()], req="SysMenuRequest"), "delete": op("系统菜单", "删除菜单", params=[pid()])},
            "/api/system/users": {"get": op("用户管理", "用户分页列表", page_of("SysUser"), page_params(q("username", "用户名模糊查询"), q("phone", "手机号模糊查询"), q("status", "状态 1/0", "integer"))), "post": op("用户管理", "新增用户", req="UserCreateRequest")},
            "/api/system/users/{id}": {"get": op("用户管理", "用户详情", ref("SysUser"), [pid()]), "put": op("用户管理", "编辑用户", params=[pid()], req="UserUpdateRequest"), "delete": op("用户管理", "删除用户", params=[pid()])},
            "/api/system/users/{id}/reset-password": {"put": op("用户管理", "重置密码", params=[pid()], req="ResetPasswordRequest")},
            "/api/system/users/{id}/status": {"put": op("用户管理", "启用/禁用", params=[pid()], req="UserStatusRequest")},
            "/api/system/roles": {"get": op("角色管理", "角色列表", arr("SysRole")), "post": op("角色管理", "新增角色", req="SysRoleRequest")},
            "/api/system/roles/{id}": {"put": op("角色管理", "编辑角色", params=[pid()], req="SysRoleRequest"), "delete": op("角色管理", "删除角色", params=[pid()])},
            "/api/system/roles/{id}/menus": {"put": op("角色管理", "角色授权菜单", params=[pid()], req="RoleMenuRequest")},
            "/api/jobs": {"get": op("职位管理", "职位分页列表", page_of("JobPosition"), page_params(q("title", "职位名称"), q("department", "部门"), q("status", "状态"))), "post": op("职位管理", "新建职位", req="JobPositionRequest")},
            "/api/jobs/{id}": {"get": op("职位管理", "职位详情", ref("JobPosition"), [pid()]), "put": op("职位管理", "编辑职位", params=[pid()], req="JobPositionRequest"), "delete": op("职位管理", "删除职位", params=[pid()])},
            "/api/jobs/{id}/publish": {"put": op("职位管理", "发布职位", params=[pid()])},
            "/api/jobs/{id}/close": {"put": op("职位管理", "关闭职位", params=[pid()])},
            "/api/resumes": {"get": op("简历管理", "简历分页列表", page_of("ResumeMain"), page_params(q("name", "姓名"), q("phone", "手机号"), q("skill", "技能"))), "post": op("简历管理", "录入简历", ref("ResumeMain"), req="ResumeRequest")},
            "/api/resumes/upload": {"post": op("简历管理", "上传简历文件", ref("ResumeMain"))},
            "/api/resumes/export": {"get": op("简历管理", "导出简历 CSV")},
            "/api/resumes/{id}": {"get": op("简历管理", "简历详情", ref("ResumeMain"), [pid()]), "put": op("简历管理", "编辑简历", params=[pid()], req="ResumeRequest"), "delete": op("简历管理", "删除简历", params=[pid()])},
            "/api/match/run": {"post": op("智能匹配", "发起匹配", arr("MatchResult"), req="RunMatchRequest")},
            "/api/match/results/{position_id}": {"get": op("智能匹配", "职位匹配结果", arr("MatchResult"), [pid("position_id")])},
            "/api/match/config": {"get": op("智能匹配", "获取配置", ref("MatchConfig")), "put": op("智能匹配", "保存配置", req="MatchConfigRequest")},
            "/api/applications": {"get": op("投递记录", "投递分页列表", page_of("JobApplication"), page_params(q("positionId", "职位 ID", "integer"), q("status", "状态"))), "post": op("投递记录", "创建投递", ref("JobApplication"), req="ApplicationCreateRequest")},
            "/api/applications/{id}": {"get": op("投递记录", "投递详情", ref("JobApplication"), [pid()])},
            "/api/applications/{id}/status": {"put": op("投递记录", "变更状态", params=[pid()], req="ApplicationStatusRequest")},
            "/api/applications/{id}/logs": {"get": op("投递记录", "操作日志", arr("ApplicationLog"), [pid()])},
            "/api/interviews": {"get": op("面试管理", "面试分页列表", page_of("InterviewRecord"), page_params(q("interviewer", "面试官"))), "post": op("面试管理", "新增面试", ref("InterviewRecord"), req="InterviewRequest")},
            "/api/interviews/{id}": {"get": op("面试管理", "面试详情", ref("InterviewRecord"), [pid()]), "put": op("面试管理", "修改面试", params=[pid()], req="InterviewRequest")},
            "/api/interviews/{id}/result": {"put": op("面试管理", "填写面试结果", params=[pid()], req="InterviewResultRequest")},
            "/api/stats/dashboard": {"get": op("统计报表", "数据看板", ref("DashboardStats"))},
            "/api/stats/source": {"get": op("统计报表", "来源统计", ref("SourceStats"), [q("startDate", "开始日期 yyyy-MM-dd"), q("endDate", "结束日期 yyyy-MM-dd")])},
        },
    }


def swagger_html() -> str:
    return """<!doctype html><html lang="zh-CN"><head><meta charset="utf-8"><title>Angu Resume Job Matcher Python API</title><link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5/swagger-ui.css"></head><body><div id="swagger-ui"></div><script src="https://unpkg.com/swagger-ui-dist@5/swagger-ui-bundle.js"></script><script>SwaggerUIBundle({url:'/v3/api-docs',dom_id:'#swagger-ui',deepLinking:true,presets:[SwaggerUIBundle.presets.apis],layout:'BaseLayout'});</script></body></html>"""
