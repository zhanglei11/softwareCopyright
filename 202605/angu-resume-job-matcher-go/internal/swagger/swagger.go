package swagger

import "encoding/json"

func Spec() []byte {
	spec := map[string]interface{}{
		"openapi": "3.0.3",
		"info": map[string]interface{}{"title": "Angu Resume Job Matcher Go API", "version": "0.1.0", "description": "Go 重写版本，接口兼容原 Vue 前端。"},
		"servers": []map[string]string{{"url": "http://127.0.0.1:19915"}},
		"components": map[string]interface{}{"securitySchemes": map[string]interface{}{"bearerAuth": map[string]string{"type": "http", "scheme": "bearer", "bearerFormat": "JWT"}}, "schemas": schemas()},
		"paths": paths(),
	}
	data, _ := json.MarshalIndent(spec, "", "  ")
	return data
}

func schemas() map[string]interface{} {
	return map[string]interface{}{
		"AjaxResult": schema("统一响应包装", props("code", "integer", "业务状态码", 200, "message", "string", "响应消息", "操作成功")),
		"PageResult": map[string]interface{}{"type": "object", "description": "分页响应结构", "properties": map[string]interface{}{"rows": map[string]interface{}{"type": "array", "description": "当前页记录", "items": map[string]string{"type": "object"}}, "total": field("integer", "总记录数", 12)}},
		"LoginRequest": schema("登录请求", props("username", "string", "用户名，不能为空", "admin", "password", "string", "密码，不能为空", "admin123")),
		"LoginResponse": schema("登录响应", props("accessToken", "string", "访问令牌", "", "refreshToken", "string", "刷新令牌", "", "userId", "integer", "用户 ID", 1, "username", "string", "用户名", "admin", "realName", "string", "真实姓名", "System Admin")),
		"SysUser": schema("系统用户，响应不包含密码", props("id", "integer", "用户 ID", 1, "username", "string", "用户名", "admin", "realName", "string", "真实姓名", "System Admin", "phone", "string", "手机号", "13800000000", "status", "integer", "状态：1 启用，0 禁用", 1)),
		"UserCreateRequest": schema("新增用户请求", props("username", "string", "用户名，唯一", "hr01", "realName", "string", "真实姓名", "招聘专员", "phone", "string", "手机号", "13800000001", "password", "string", "初始密码", "Admin@123")),
		"UserUpdateRequest": schema("编辑用户请求", props("realName", "string", "真实姓名", "", "phone", "string", "手机号", "", "status", "integer", "状态：1 启用，0 禁用", 1)),
		"SysMenu": schema("系统菜单树节点", props("id", "integer", "菜单 ID", 1, "parentId", "integer", "父菜单 ID", 0, "menuType", "integer", "类型：0 目录，1 菜单，2 按钮", 0, "menuName", "string", "菜单名称", "System", "path", "string", "前端路由", "/system", "permCode", "string", "权限标识", "system:user:list")),
		"SysRole": schema("系统角色", props("id", "integer", "角色 ID", 1, "roleName", "string", "角色名称", "Super Admin", "roleCode", "string", "角色标识", "SUPER_ADMIN", "builtin", "integer", "是否内置", 1, "status", "integer", "状态", 1)),
		"JobPositionRequest": schema("职位创建/编辑请求", props("title", "string", "职位名称，不能为空", "Go 后端工程师", "department", "string", "所属部门", "智能招聘部", "jobType", "string", "岗位类型：FULL_TIME/PART_TIME/INTERN", "FULL_TIME", "location", "string", "工作地点", "杭州", "salaryMin", "integer", "薪资下限", 15000, "salaryMax", "integer", "薪资上限", 25000, "eduRequire", "string", "学历要求", "BACHELOR", "expRequire", "integer", "经验年限", 3, "description", "string", "职位描述", "")),
		"JobPosition": schema("职位信息响应", props("id", "integer", "职位 ID", 1, "title", "string", "职位名称", "Go 后端工程师", "status", "string", "状态：DRAFT/OPEN/CLOSED", "OPEN", "skillTags", "array", "技能标签", []string{"Go"})),
		"ResumeRequest": schema("简历创建/编辑请求", props("name", "string", "候选人姓名", "张三", "phone", "string", "手机号", "13900000000", "email", "string", "邮箱", "a@example.com", "city", "string", "当前城市", "杭州", "desiredPosition", "string", "期望职位", "Go 工程师", "skills", "array", "技能名称列表", []string{"Go", "后端"})),
		"ResumeMain": schema("简历响应", props("id", "integer", "简历 ID", 1, "name", "string", "候选人姓名", "张三", "skills", "array", "技能对象列表", []interface{}{})),
		"RunMatchRequest": schema("发起匹配请求", props("positionId", "integer", "职位 ID，职位必须为 OPEN", 1)),
		"MatchResult": schema("匹配结果", props("positionId", "integer", "职位 ID", 1, "resumeId", "integer", "简历 ID", 1, "totalScore", "number", "综合分", 63.33, "skillScore", "number", "技能分", 66.67)),
		"ApplicationCreateRequest": schema("创建投递记录请求", props("positionId", "integer", "职位 ID", 1, "resumeId", "integer", "简历 ID", 1, "remark", "string", "备注", "")),
		"ApplicationStatusRequest": schema("变更投递状态请求，合法流转同 Java 版本", props("status", "string", "目标状态", "RESUME_PASSED", "remark", "string", "备注", "")),
		"JobApplication": schema("投递记录", props("id", "integer", "投递 ID", 1, "status", "string", "投递状态", "PENDING", "positionTitle", "string", "职位名称", "", "resumeName", "string", "候选人姓名", "")),
		"InterviewRequest": schema("新增/编辑面试请求", props("applicationId", "integer", "投递 ID", 1, "interviewTime", "string", "面试时间 yyyy-MM-dd HH:mm:ss", "2026-06-22 15:00:00", "interviewer", "string", "面试官", "面试官A", "location", "string", "面试地点", "线上")),
		"InterviewResultRequest": schema("填写面试结果，PASS 推进到 INTERVIEW_PASSED", props("score", "integer", "评分 1-5", 5, "comment", "string", "评价", "通过", "result", "string", "结果 PASS/REJECT", "PASS")),
		"InterviewRecord": schema("面试记录", props("id", "integer", "面试 ID", 1, "applicationId", "integer", "投递 ID", 1, "interviewer", "string", "面试官", "面试官A", "result", "string", "结果", "PASS")),
	}
}

func paths() map[string]interface{} {
	return map[string]interface{}{
		"/api/auth/login": post("认证管理", "登录", "LoginRequest", ref("LoginResponse")),
		"/api/auth/me": get("认证管理", "当前用户", ref("SysUser")),
		"/api/system/menus/tree": get("系统菜单", "菜单树", arr("SysMenu")),
		"/api/system/users": map[string]interface{}{"get": op("用户管理", "用户分页列表", nil, pageOf("SysUser")), "post": op("用户管理", "新增用户", ref("UserCreateRequest"), nil)},
		"/api/system/roles": get("角色管理", "角色列表", arr("SysRole")),
		"/api/jobs": map[string]interface{}{"get": op("职位管理", "职位分页列表", nil, pageOf("JobPosition")), "post": op("职位管理", "新建职位", ref("JobPositionRequest"), nil)},
		"/api/jobs/{id}": map[string]interface{}{"get": op("职位管理", "职位详情", nil, ref("JobPosition")), "put": op("职位管理", "编辑职位", ref("JobPositionRequest"), nil), "delete": op("职位管理", "删除职位", nil, nil)},
		"/api/resumes": map[string]interface{}{"get": op("简历管理", "简历分页列表", nil, pageOf("ResumeMain")), "post": op("简历管理", "录入简历", ref("ResumeRequest"), ref("ResumeMain"))},
		"/api/match/run": post("智能匹配", "发起匹配", "RunMatchRequest", arr("MatchResult")),
		"/api/applications": map[string]interface{}{"get": op("投递记录", "投递分页列表", nil, pageOf("JobApplication")), "post": op("投递记录", "创建投递", ref("ApplicationCreateRequest"), ref("JobApplication"))},
		"/api/interviews": map[string]interface{}{"get": op("面试管理", "面试分页列表", nil, pageOf("InterviewRecord")), "post": op("面试管理", "新增面试", ref("InterviewRequest"), ref("InterviewRecord"))},
		"/api/stats/dashboard": get("统计报表", "数据看板", map[string]string{"type": "object"}),
	}
}

func HTML() string {
	return `<!doctype html><html lang="zh-CN"><head><meta charset="utf-8"><title>Angu Resume Job Matcher Go API</title><link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5/swagger-ui.css"></head><body><div id="swagger-ui"></div><script src="https://unpkg.com/swagger-ui-dist@5/swagger-ui-bundle.js"></script><script>SwaggerUIBundle({url:'/v3/api-docs',dom_id:'#swagger-ui',deepLinking:true,presets:[SwaggerUIBundle.presets.apis],layout:'BaseLayout'});</script></body></html>`
}

func schema(desc string, properties map[string]interface{}) map[string]interface{} {
	return map[string]interface{}{"type": "object", "description": desc, "properties": properties}
}

func props(items ...interface{}) map[string]interface{} {
	out := map[string]interface{}{}
	for i := 0; i+3 < len(items); i += 4 {
		out[items[i].(string)] = field(items[i+1].(string), items[i+2].(string), items[i+3])
	}
	return out
}

func field(typ, desc string, example interface{}) map[string]interface{} {
	return map[string]interface{}{"type": typ, "description": desc, "example": example}
}

func ref(name string) map[string]string { return map[string]string{"$ref": "#/components/schemas/" + name} }
func arr(name string) map[string]interface{} { return map[string]interface{}{"type": "array", "items": ref(name)} }
func pageOf(name string) map[string]interface{} { return map[string]interface{}{"allOf": []interface{}{ref("PageResult"), map[string]interface{}{"type": "object", "properties": map[string]interface{}{"rows": arr(name)}}}} }

func get(tag, summary string, resp interface{}) map[string]interface{} { return map[string]interface{}{"get": op(tag, summary, nil, resp)} }
func post(tag, summary, req string, resp interface{}) map[string]interface{} { return map[string]interface{}{"post": op(tag, summary, ref(req), resp)} }

func op(tag, summary string, request, response interface{}) map[string]interface{} {
	item := map[string]interface{}{"tags": []string{tag}, "summary": summary, "responses": map[string]interface{}{"200": map[string]interface{}{"description": "成功", "content": map[string]interface{}{"application/json": map[string]interface{}{"schema": map[string]interface{}{"allOf": []interface{}{ref("AjaxResult"), map[string]interface{}{"type": "object", "properties": map[string]interface{}{"data": response}}}}}}}}}
	if request != nil {
		item["requestBody"] = map[string]interface{}{"required": true, "content": map[string]interface{}{"application/json": map[string]interface{}{"schema": request}}}
	}
	return item
}
