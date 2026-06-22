async fn openapi_json() -> Json<Value> {
    ok_openapi()
}

fn ok_openapi() -> Json<Value> {
    Json(openapi_spec())
}

async fn swagger_ui() -> impl IntoResponse {
    let html = r#"<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
  <title>Angu Resume Job Matcher API</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5/swagger-ui.css" />
  <style>body{margin:0;background:#f7f8fb}.topbar{display:none}</style>
</head>
<body>
  <div id="swagger-ui"></div>
  <script src="https://unpkg.com/swagger-ui-dist@5/swagger-ui-bundle.js"></script>
  <script>
    window.ui = SwaggerUIBundle({
      url: '/v3/api-docs',
      dom_id: '#swagger-ui',
      deepLinking: true,
      presets: [SwaggerUIBundle.presets.apis],
      layout: 'BaseLayout'
    })
  </script>
</body>
</html>"#;
    ([(
        header::CONTENT_TYPE,
        HeaderValue::from_static("text/html; charset=utf-8"),
    )], html)
}

fn openapi_spec() -> Value {
    json!({
        "openapi": "3.0.3",
        "info": {
            "title": "Angu Resume Job Matcher Rust API",
            "version": "0.1.0",
            "description": "Rust/Axum 复刻后端接口文档，兼容原 Vue 前端调用路径。"
        },
        "servers": [{ "url": "http://127.0.0.1:19915" }],
        "tags": [
            {"name":"认证管理"},{"name":"系统菜单"},{"name":"用户管理"},{"name":"角色管理"},
            {"name":"职位管理"},{"name":"简历管理"},{"name":"智能匹配"},{"name":"投递记录"},
            {"name":"面试管理"},{"name":"统计报表"}
        ],
        "components": {
            "securitySchemes": {
                "bearerAuth": {"type":"http","scheme":"bearer","bearerFormat":"JWT"}
            },
            "schemas": schemas()
        },
        "security": [{"bearerAuth": []}],
        "paths": swagger_paths()
    })
}

fn schemas() -> Value {
    json!({
        "AjaxResult": {
            "type":"object",
            "description":"统一响应包装。业务成功 code=200；业务失败时 code 为 400/401/403/404/409/422/500 等。",
            "properties": {
                "code":{"type":"integer","description":"业务状态码","example":200},
                "message":{"type":"string","description":"响应消息","example":"操作成功"},
                "data":{"description":"响应数据。不同接口返回不同结构；无数据时为 null。"}
            }
        },
        "EmptyData": {"description":"无返回数据，data 为 null","nullable":true},
        "PageResult": {
            "type":"object",
            "description":"分页响应数据，等价于 Java TableDataInfo。",
            "properties": {
                "rows":{"type":"array","description":"当前页数据列表","items":{"type":"object"}},
                "total":{"type":"integer","description":"符合条件的总记录数","example":12}
            }
        },
        "LoginRequest": {
            "type":"object",
            "description":"登录请求",
            "required":["username","password"],
            "properties":{
                "username":{"type":"string","description":"用户名，不能为空","example":"admin"},
                "password":{"type":"string","description":"密码，不能为空","example":"admin123"}
            }
        },
        "RefreshTokenRequest": {
            "type":"object",
            "description":"刷新 Token 请求",
            "required":["refreshToken"],
            "properties":{"refreshToken":{"type":"string","description":"登录接口返回的刷新令牌","example":"eyJhbGciOiJIUzI1NiJ9..."}}
        },
        "LoginResponse": {
            "type":"object",
            "description":"登录或刷新 Token 响应",
            "properties":{
                "accessToken":{"type":"string","description":"访问令牌，前端放入 Authorization: Bearer <token>","example":"eyJhbGciOiJIUzI1NiJ9..."},
                "refreshToken":{"type":"string","description":"刷新令牌，用于换取新的 accessToken","example":"eyJhbGciOiJIUzI1NiJ9..."},
                "userId":{"type":"integer","description":"当前用户 ID","example":1},
                "username":{"type":"string","description":"用户名","example":"admin"},
                "realName":{"type":"string","description":"真实姓名","example":"System Admin"}
            }
        },
        "UserCreateRequest": {
            "type":"object",
            "description":"新增用户请求",
            "required":["username","realName","phone","password"],
            "properties":{
                "username":{"type":"string","description":"用户名，唯一，不能为空","example":"hr01"},
                "realName":{"type":"string","description":"真实姓名，不能为空","example":"招聘专员"},
                "phone":{"type":"string","description":"手机号，唯一，不能为空","example":"13800000001"},
                "password":{"type":"string","description":"初始密码，不能为空","example":"Admin@123"},
                "roleIds":{"type":"array","description":"绑定角色 ID 列表","items":{"type":"integer"},"example":[2,3]},
                "status":{"type":"integer","description":"用户状态：1 启用，0 禁用。新增时默认启用。","example":1}
            }
        },
        "UserUpdateRequest": {
            "type":"object",
            "description":"编辑用户请求",
            "properties":{
                "realName":{"type":"string","description":"真实姓名","example":"HR 主管"},
                "phone":{"type":"string","description":"手机号","example":"13800000002"},
                "status":{"type":"integer","description":"用户状态：1 启用，0 禁用","example":1},
                "roleIds":{"type":"array","description":"新的角色 ID 列表；传入时会覆盖原角色绑定","items":{"type":"integer"},"example":[2]}
            }
        },
        "ResetPasswordRequest": {
            "type":"object",
            "description":"重置密码请求",
            "required":["newPassword"],
            "properties":{"newPassword":{"type":"string","description":"新密码，不能为空","example":"Admin@123"}}
        },
        "UserStatusRequest": {
            "type":"object",
            "description":"启用/禁用用户请求",
            "required":["status"],
            "properties":{"status":{"type":"integer","description":"用户状态：1 启用，0 禁用","example":0}}
        },
        "SysUser": {
            "type":"object",
            "description":"系统用户。密码字段不会返回给前端。",
            "properties":{
                "id":{"type":"integer","description":"用户 ID","example":1},
                "username":{"type":"string","description":"用户名","example":"admin"},
                "realName":{"type":"string","description":"真实姓名","example":"System Admin"},
                "phone":{"type":"string","description":"手机号","example":"13800000000"},
                "status":{"type":"integer","description":"状态：1 启用，0 禁用","example":1},
                "errorCount":{"type":"integer","description":"登录错误次数","example":0},
                "lockedUntil":{"type":"string","nullable":true,"description":"锁定截止时间，未锁定为 null","example":null},
                "deleted":{"type":"integer","description":"逻辑删除标记：0 正常，1 删除","example":0},
                "createdTime":{"type":"string","description":"创建时间，格式 yyyy-MM-dd HH:mm:ss","example":"2026-05-27 14:37:46"},
                "updatedTime":{"type":"string","description":"更新时间，格式 yyyy-MM-dd HH:mm:ss","example":"2026-05-27 14:37:46"},
                "roleIds":{"type":"array","description":"用户绑定角色 ID 列表，详情接口返回","items":{"type":"integer"},"example":[1]}
            }
        },
        "SysRole": {
            "type":"object",
            "description":"系统角色",
            "properties":{
                "id":{"type":"integer","description":"角色 ID","example":1},
                "roleName":{"type":"string","description":"角色名称","example":"Super Admin"},
                "roleCode":{"type":"string","description":"角色标识，唯一","example":"SUPER_ADMIN"},
                "builtin":{"type":"integer","description":"是否内置角色：1 是，0 否。内置角色不可编辑/删除。","example":1},
                "status":{"type":"integer","description":"状态：1 启用，0 禁用","example":1},
                "remark":{"type":"string","nullable":true,"description":"备注","example":"Built-in super admin"},
                "createdTime":{"type":"string","description":"创建时间","example":"2026-05-27 14:37:46"},
                "updatedTime":{"type":"string","description":"更新时间","example":"2026-05-27 14:37:46"}
            }
        },
        "SysRoleRequest": {
            "type":"object",
            "description":"新增/编辑角色请求",
            "properties":{
                "roleName":{"type":"string","description":"角色名称","example":"HR Admin"},
                "roleCode":{"type":"string","description":"角色标识，唯一","example":"HR_ADMIN"},
                "status":{"type":"integer","description":"状态：1 启用，0 禁用","example":1},
                "remark":{"type":"string","description":"备注","example":"HR admin"}
            }
        },
        "RoleMenuRequest": {
            "type":"object",
            "description":"角色授权菜单请求",
            "properties":{"menuIds":{"type":"array","description":"授权菜单/按钮 ID 列表","items":{"type":"integer"},"example":[1,2,11,1101]}}
        },
        "SysMenu": {
            "type":"object",
            "description":"系统菜单树节点",
            "properties":{
                "id":{"type":"integer","description":"菜单 ID","example":1},
                "parentId":{"type":"integer","description":"父菜单 ID，根节点为 0","example":0},
                "menuType":{"type":"integer","description":"菜单类型：0 目录，1 菜单，2 按钮权限","example":0},
                "menuName":{"type":"string","description":"菜单名称","example":"System"},
                "path":{"type":"string","nullable":true,"description":"前端路由路径","example":"/system"},
                "permCode":{"type":"string","nullable":true,"description":"权限标识，按钮权限使用","example":"system:user:list"},
                "icon":{"type":"string","nullable":true,"description":"前端图标标识","example":"setting"},
                "sort":{"type":"integer","description":"排序值，越小越靠前","example":1},
                "children":{"type":"array","description":"子菜单列表","items":{"$ref":"#/components/schemas/SysMenu"}}
            }
        },
        "SysMenuRequest": {
            "type":"object",
            "description":"新增/编辑菜单请求",
            "properties":{
                "id":{"type":"integer","description":"菜单 ID；新增时可不传，由服务端生成","example":10001},
                "parentId":{"type":"integer","description":"父菜单 ID，根节点为 0","example":1},
                "menuType":{"type":"integer","description":"菜单类型：0 目录，1 菜单，2 按钮权限","example":1},
                "menuName":{"type":"string","description":"菜单名称","example":"Users"},
                "path":{"type":"string","description":"前端路由路径","example":"/system/users"},
                "permCode":{"type":"string","description":"权限标识","example":"system:user:list"},
                "icon":{"type":"string","description":"图标标识","example":"user"},
                "sort":{"type":"integer","description":"排序值","example":1}
            }
        },
        "JobPositionRequest": {
            "type":"object",
            "description":"职位创建/编辑请求",
            "required":["title"],
            "properties": {
                "title":{"type":"string","description":"职位名称，不能为空","example":"Rust 后端工程师"},
                "department":{"type":"string","description":"所属部门","example":"智能招聘部"},
                "jobType":{"type":"string","description":"岗位类型","enum":["FULL_TIME","PART_TIME","INTERN"],"example":"FULL_TIME"},
                "location":{"type":"string","description":"工作地点","example":"杭州"},
                "salaryMin":{"type":"integer","description":"薪资下限","example":15000},
                "salaryMax":{"type":"integer","description":"薪资上限","example":25000},
                "eduRequire":{"type":"string","description":"学历要求","enum":["HIGH_SCHOOL","ASSOCIATE","BACHELOR","MASTER","DOCTOR"],"example":"BACHELOR"},
                "expRequire":{"type":"integer","description":"经验年限要求","example":3},
                "description":{"type":"string","description":"职位描述","example":"负责 Rust 后端服务开发。"},
                "skillTags":{"type":"array","description":"职位技能标签，用于智能匹配技能分","items":{"type":"string"},"example":["Rust","Axum","SQLite"]}
            }
        },
        "JobPosition": {
            "allOf":[{"$ref":"#/components/schemas/JobPositionRequest"},{"type":"object","description":"职位信息","properties":{
                "id":{"type":"integer","description":"职位 ID","example":1},
                "status":{"type":"string","description":"职位状态：DRAFT 草稿，OPEN 发布中，CLOSED 已关闭","enum":["DRAFT","OPEN","CLOSED"],"example":"OPEN"},
                "deleted":{"type":"integer","description":"逻辑删除标记","example":0},
                "creatorId":{"type":"integer","description":"创建人用户 ID","example":1},
                "createdTime":{"type":"string","description":"创建时间","example":"2026-05-27 16:17:13"},
                "updatedTime":{"type":"string","description":"更新时间","example":"2026-05-27 16:18:52"}
            }}]
        },
        "ResumeEducation": {
            "type":"object",
            "description":"教育经历",
            "properties":{
                "id":{"type":"integer","description":"教育经历 ID，新增时可不传","example":1},
                "resumeId":{"type":"integer","description":"所属简历 ID，新增时可不传","example":1},
                "school":{"type":"string","description":"学校名称","example":"浙江大学"},
                "major":{"type":"string","description":"专业","example":"软件工程"},
                "eduLevel":{"type":"string","description":"学历等级","enum":["HIGH_SCHOOL","ASSOCIATE","BACHELOR","MASTER","DOCTOR"],"example":"BACHELOR"},
                "startDate":{"type":"string","description":"开始日期，兼容 yyyy-MM 或 yyyy-MM-dd","example":"2018-09-01"},
                "endDate":{"type":"string","description":"结束日期，兼容 yyyy-MM 或 yyyy-MM-dd","example":"2022-06-01"}
            }
        },
        "ResumeWorkExp": {
            "type":"object",
            "description":"工作经历",
            "properties":{
                "id":{"type":"integer","description":"工作经历 ID，新增时可不传","example":1},
                "resumeId":{"type":"integer","description":"所属简历 ID，新增时可不传","example":1},
                "company":{"type":"string","description":"公司名称","example":"安谷科技"},
                "position":{"type":"string","description":"职位名称","example":"后端工程师"},
                "industry":{"type":"string","description":"所属行业","example":"软件服务"},
                "startDate":{"type":"string","description":"开始日期，兼容 yyyy-MM 或 yyyy-MM-dd","example":"2022-07-01"},
                "endDate":{"type":"string","description":"结束日期，兼容 yyyy-MM 或 yyyy-MM-dd","example":"2025-06-01"},
                "description":{"type":"string","description":"工作内容描述","example":"负责招聘系统后端服务开发。"}
            }
        },
        "ResumeSkill": {
            "type":"object",
            "description":"简历技能标签",
            "properties":{
                "id":{"type":"integer","description":"技能 ID","example":1},
                "resumeId":{"type":"integer","description":"所属简历 ID","example":1},
                "skillName":{"type":"string","description":"技能名称","example":"Rust"}
            }
        },
        "ResumeRequest": {
            "type":"object",
            "description":"简历创建/编辑请求",
            "properties": {
                "name":{"type":"string","description":"候选人姓名","example":"张三"},
                "phone":{"type":"string","description":"手机号","example":"13900000000"},
                "email":{"type":"string","description":"邮箱","example":"zhangsan@example.com"},
                "gender":{"type":"integer","description":"性别：可按前端约定传 0/1/2","example":1},
                "birthDate":{"type":"string","description":"出生日期，格式 yyyy-MM-dd","example":"1998-01-01"},
                "city":{"type":"string","description":"当前城市","example":"杭州"},
                "desiredPosition":{"type":"string","description":"期望职位","example":"Rust 后端工程师"},
                "desiredCity":{"type":"string","description":"期望城市","example":"杭州"},
                "desiredSalaryMin":{"type":"integer","description":"期望薪资下限","example":15000},
                "desiredSalaryMax":{"type":"integer","description":"期望薪资上限","example":25000},
                "jobStatus":{"type":"string","description":"求职状态","example":"ACTIVE"},
                "source":{"type":"string","description":"简历来源，默认 MANUAL；上传文件为 FILE","example":"MANUAL"},
                "selfIntro":{"type":"string","description":"个人简介","example":"三年后端开发经验。"},
                "skills":{"type":"array","description":"技能名称列表","items":{"type":"string"},"example":["Rust","Axum"]},
                "educations":{"type":"array","description":"教育经历列表","items":{"$ref":"#/components/schemas/ResumeEducation"}},
                "workExps":{"type":"array","description":"工作经历列表","items":{"$ref":"#/components/schemas/ResumeWorkExp"}}
            }
        },
        "ResumeMain": {
            "allOf":[{"$ref":"#/components/schemas/ResumeRequest"},{"type":"object","description":"简历主表响应","properties":{
                "id":{"type":"integer","description":"简历 ID","example":1},
                "highestEdu":{"type":"string","nullable":true,"description":"最高学历，由教育经历推导或直接保存","example":"BACHELOR"},
                "totalExpYears":{"type":"integer","nullable":true,"description":"总工作年限，当前实现按工作经历数量粗略推导","example":3},
                "filePath":{"type":"string","nullable":true,"description":"上传文件保存名；结构化录入为空","example":"d6b7f... .pdf"},
                "parseSuccess":{"type":"integer","description":"文件解析是否成功：0 未解析/失败，1 成功","example":0},
                "deleted":{"type":"integer","description":"逻辑删除标记","example":0},
                "creatorId":{"type":"integer","description":"创建人用户 ID","example":1},
                "createdTime":{"type":"string","description":"创建时间","example":"2026-05-27 16:18:40"},
                "updatedTime":{"type":"string","description":"更新时间","example":"2026-05-27 16:18:40"},
                "skills":{"type":"array","description":"技能标签对象列表","items":{"$ref":"#/components/schemas/ResumeSkill"}},
                "educations":{"type":"array","description":"教育经历列表","items":{"$ref":"#/components/schemas/ResumeEducation"}},
                "workExps":{"type":"array","description":"工作经历列表","items":{"$ref":"#/components/schemas/ResumeWorkExp"}}
            }}]
        },
        "MatchConfigRequest": {
            "type":"object",
            "description":"匹配规则配置请求。三个权重总和必须等于 100。",
            "required":["skillWeight","eduWeight","expWeight"],
            "properties":{
                "skillWeight":{"type":"integer","description":"技能匹配权重百分比","example":50},
                "eduWeight":{"type":"integer","description":"学历匹配权重百分比","example":30},
                "expWeight":{"type":"integer","description":"经验匹配权重百分比","example":20}
            }
        },
        "MatchConfig": {
            "allOf":[{"$ref":"#/components/schemas/MatchConfigRequest"},{"type":"object","description":"匹配规则配置响应","properties":{
                "id":{"type":"integer","description":"配置 ID，固定为 1","example":1},
                "updatedTime":{"type":"string","description":"更新时间","example":"2026-05-27 16:59:08"},
                "updaterId":{"type":"integer","description":"更新人用户 ID","example":1}
            }}]
        },
        "RunMatchRequest": {
            "type":"object",
            "description":"发起匹配请求",
            "required":["positionId"],
            "properties":{"positionId":{"type":"integer","description":"要匹配的职位 ID；职位必须为 OPEN","example":1}}
        },
        "MatchResult": {
            "type":"object",
            "description":"匹配结果",
            "properties":{
                "id":{"type":"integer","description":"匹配结果 ID","example":1},
                "positionId":{"type":"integer","description":"职位 ID","example":1},
                "resumeId":{"type":"integer","description":"简历 ID","example":1},
                "totalScore":{"type":"number","format":"double","description":"综合匹配分","example":63.33},
                "skillScore":{"type":"number","format":"double","description":"技能匹配分","example":66.67},
                "eduScore":{"type":"number","format":"double","description":"学历匹配分","example":100.0},
                "expScore":{"type":"number","format":"double","description":"经验匹配分","example":0.0},
                "matchedAt":{"type":"string","description":"匹配时间","example":"2026-06-22 12:05:06"},
                "resumeName":{"type":"string","description":"候选人姓名","example":"张三"},
                "resumePhone":{"type":"string","description":"候选人手机号","example":"13900000000"}
            }
        },
        "ApplicationCreateRequest": {
            "type":"object",
            "description":"创建投递记录请求",
            "required":["positionId","resumeId"],
            "properties":{
                "positionId":{"type":"integer","description":"职位 ID，职位必须为 OPEN","example":1},
                "resumeId":{"type":"integer","description":"简历 ID","example":1},
                "remark":{"type":"string","description":"备注","example":"匹配后人工投递"}
            }
        },
        "ApplicationStatusRequest": {
            "type":"object",
            "description":"变更投递状态请求。合法流转：PENDING->RESUME_PASSED/RESUME_REJECTED，RESUME_PASSED->INTERVIEW_WAITING，INTERVIEW_WAITING->INTERVIEWING，INTERVIEWING->INTERVIEW_PASSED/INTERVIEW_REJECTED，INTERVIEW_PASSED->HIRED。",
            "required":["status"],
            "properties":{
                "status":{"type":"string","description":"目标状态","enum":["RESUME_PASSED","RESUME_REJECTED","INTERVIEW_WAITING","INTERVIEWING","INTERVIEW_PASSED","INTERVIEW_REJECTED","HIRED"],"example":"RESUME_PASSED"},
                "remark":{"type":"string","description":"状态流转备注，会写入操作日志","example":"简历筛选通过"}
            }
        },
        "JobApplication": {
            "type":"object",
            "description":"投递记录",
            "properties":{
                "id":{"type":"integer","description":"投递记录 ID","example":1},
                "positionId":{"type":"integer","description":"职位 ID","example":1},
                "resumeId":{"type":"integer","description":"简历 ID","example":1},
                "status":{"type":"string","description":"投递状态","example":"INTERVIEW_PASSED"},
                "operateTime":{"type":"string","description":"最近操作时间","example":"2026-05-27 16:29:53"},
                "operatorId":{"type":"integer","nullable":true,"description":"最近操作人 ID","example":1},
                "remark":{"type":"string","nullable":true,"description":"最近操作备注","example":"面试结果：PASS"},
                "createdTime":{"type":"string","description":"创建时间","example":"2026-05-27 16:19:47"},
                "positionTitle":{"type":"string","nullable":true,"description":"职位名称","example":"自动化测试职位-0527"},
                "resumeName":{"type":"string","nullable":true,"description":"候选人姓名","example":"自动化测试候选人0527"}
            }
        },
        "ApplicationLog": {
            "type":"object",
            "description":"投递操作日志",
            "properties":{
                "id":{"type":"integer","description":"日志 ID","example":1},
                "applicationId":{"type":"integer","description":"投递记录 ID","example":1},
                "fromStatus":{"type":"string","nullable":true,"description":"原状态","example":"PENDING"},
                "toStatus":{"type":"string","description":"目标状态","example":"RESUME_PASSED"},
                "operatorId":{"type":"integer","nullable":true,"description":"操作人 ID","example":1},
                "operatorName":{"type":"string","nullable":true,"description":"操作人姓名","example":"System Admin"},
                "remark":{"type":"string","nullable":true,"description":"操作备注","example":"简历通过"},
                "createdTime":{"type":"string","description":"操作时间","example":"2026-05-27 16:29:44"}
            }
        },
        "InterviewRequest": {
            "type":"object",
            "description":"新增/编辑面试安排请求",
            "required":["applicationId"],
            "properties":{
                "applicationId":{"type":"integer","description":"投递记录 ID；创建面试时必填","example":1},
                "interviewTime":{"type":"string","description":"面试时间，格式 yyyy-MM-dd HH:mm:ss","example":"2026-06-22 14:00:00"},
                "interviewer":{"type":"string","description":"面试官","example":"面试官A"},
                "location":{"type":"string","description":"面试地点/会议室/线上会议地址","example":"线上会议室"}
            }
        },
        "InterviewResultRequest": {
            "type":"object",
            "description":"填写面试结果请求。result=PASS 时投递状态流转为 INTERVIEW_PASSED，否则流转为 INTERVIEW_REJECTED。",
            "required":["result"],
            "properties":{
                "score":{"type":"integer","minimum":1,"maximum":5,"description":"面试评分，1-5 分","example":5},
                "comment":{"type":"string","description":"面试评价","example":"技术能力符合要求"},
                "result":{"type":"string","description":"面试结果","enum":["PASS","REJECT"],"example":"PASS"}
            }
        },
        "InterviewRecord": {
            "type":"object",
            "description":"面试记录",
            "properties":{
                "id":{"type":"integer","description":"面试记录 ID","example":1},
                "applicationId":{"type":"integer","description":"投递记录 ID","example":1},
                "interviewTime":{"type":"string","nullable":true,"description":"面试时间","example":"2026-05-27 19:30:00"},
                "interviewer":{"type":"string","nullable":true,"description":"面试官","example":"面试官A"},
                "location":{"type":"string","nullable":true,"description":"面试地点","example":"线上会议室"},
                "score":{"type":"integer","nullable":true,"description":"评分，1-5 分","example":5},
                "comment":{"type":"string","nullable":true,"description":"评价","example":"接口测试面试通过"},
                "result":{"type":"string","nullable":true,"description":"结果：PASS/REJECT","example":"PASS"},
                "createdTime":{"type":"string","description":"创建时间","example":"2026-05-27 16:29:44"}
            }
        },
        "DashboardStats": {
            "type":"object",
            "description":"数据看板统计",
            "properties":{
                "monthlyApplications":{"type":"integer","description":"本月投递数","example":12},
                "monthlyInterviewDone":{"type":"integer","description":"本月已完成面试数","example":5},
                "monthlyHired":{"type":"integer","description":"本月录用数","example":2},
                "funnel":{"type":"object","description":"招聘漏斗","properties":{
                    "applied":{"type":"integer","description":"已投递","example":12},
                    "passed":{"type":"integer","description":"简历通过及后续阶段","example":8},
                    "interviewed":{"type":"integer","description":"进入面试及后续阶段","example":5},
                    "hired":{"type":"integer","description":"已录用","example":2}
                }}
            }
        },
        "SourceStats": {
            "type":"object",
            "description":"简历来源统计",
            "properties":{
                "distribution":{"type":"array","description":"来源分布列表","items":{"type":"object","properties":{
                    "source":{"type":"string","description":"简历来源","example":"MANUAL"},
                    "cnt":{"type":"integer","description":"数量","example":2}
                }}}
            }
        }
    })
}

fn ajax_response(desc: &str) -> Value {
    json!({
        "description": desc,
        "content": {"application/json": {"schema": {"$ref":"#/components/schemas/AjaxResult"}}}
    })
}

fn ajax_response_data(desc: &str, data_schema: Value) -> Value {
    json!({
        "description": desc,
        "content": {
            "application/json": {
                "schema": {
                    "allOf": [
                        {"$ref":"#/components/schemas/AjaxResult"},
                        {"type":"object","properties":{"data": data_schema}}
                    ]
                }
            }
        }
    })
}

fn schema_ref(schema: &str) -> Value {
    json!({"$ref": format!("#/components/schemas/{schema}")})
}

fn array_of(schema: &str) -> Value {
    json!({"type":"array","items": schema_ref(schema)})
}

fn page_of(schema: &str) -> Value {
    json!({
        "allOf": [
            {"$ref":"#/components/schemas/PageResult"},
            {"type":"object","properties":{"rows":{"type":"array","description":"当前页数据列表","items":schema_ref(schema)}}}
        ]
    })
}

fn body_ref(schema: &str) -> Value {
    json!({
        "required": true,
        "content": {"application/json": {"schema": {"$ref": format!("#/components/schemas/{schema}")}}}
    })
}

fn path_id(name: &str) -> Value {
    json!({"name":name,"in":"path","required":true,"schema":{"type":"integer"}})
}

fn pagination_params(extra: Vec<Value>) -> Value {
    let mut params = vec![
        json!({"name":"page","in":"query","schema":{"type":"integer","default":1}}),
        json!({"name":"size","in":"query","schema":{"type":"integer","default":20}})
    ];
    params.extend(extra);
    Value::Array(params)
}

fn swagger_paths() -> Value {
    let mut paths = serde_json::Map::new();
    macro_rules! op {
        ($tag:expr, $summary:expr) => {
            json!({"tags":[$tag],"summary":$summary,"responses":{"200":ajax_response("成功")}})
        };
        ($tag:expr, $summary:expr, response $data:expr) => {
            json!({"tags":[$tag],"summary":$summary,"responses":{"200":ajax_response_data("成功",$data)}})
        };
        ($tag:expr, $summary:expr, body $schema:expr) => {
            json!({"tags":[$tag],"summary":$summary,"requestBody":body_ref($schema),"responses":{"200":ajax_response("成功")}})
        };
        ($tag:expr, $summary:expr, body $schema:expr, response $data:expr) => {
            json!({"tags":[$tag],"summary":$summary,"requestBody":body_ref($schema),"responses":{"200":ajax_response_data("成功",$data)}})
        };
        ($tag:expr, $summary:expr, params $params:expr) => {
            json!({"tags":[$tag],"summary":$summary,"parameters":$params,"responses":{"200":ajax_response("成功")}})
        };
        ($tag:expr, $summary:expr, params $params:expr, response $data:expr) => {
            json!({"tags":[$tag],"summary":$summary,"parameters":$params,"responses":{"200":ajax_response_data("成功",$data)}})
        };
        ($tag:expr, $summary:expr, params $params:expr, body $schema:expr) => {
            json!({"tags":[$tag],"summary":$summary,"parameters":$params,"requestBody":body_ref($schema),"responses":{"200":ajax_response("成功")}})
        };
        ($tag:expr, $summary:expr, params $params:expr, body $schema:expr, response $data:expr) => {
            json!({"tags":[$tag],"summary":$summary,"parameters":$params,"requestBody":body_ref($schema),"responses":{"200":ajax_response_data("成功",$data)}})
        };
    }

    paths.insert("/api/auth/login".into(), json!({"post": op!("认证管理","登录", body "LoginRequest", response schema_ref("LoginResponse"))}));
    paths.insert("/api/auth/refresh".into(), json!({"post": op!("认证管理","刷新 Token", body "RefreshTokenRequest", response schema_ref("LoginResponse"))}));
    paths.insert("/api/auth/logout".into(), json!({"post": op!("认证管理","退出登录")}));
    paths.insert("/api/auth/me".into(), json!({"get": op!("认证管理","当前用户", response schema_ref("SysUser"))}));

    paths.insert("/api/system/menus/tree".into(), json!({"get": op!("系统菜单","菜单树", response array_of("SysMenu"))}));
    paths.insert("/api/system/menus/my-tree".into(), json!({"get": op!("系统菜单","当前用户菜单树", response array_of("SysMenu"))}));
    paths.insert("/api/system/menus".into(), json!({"post": op!("系统菜单","新增菜单", body "SysMenuRequest")}));
    paths.insert("/api/system/menus/{id}".into(), json!({"put": op!("系统菜单","编辑菜单", params vec![path_id("id")], body "SysMenuRequest"),"delete": op!("系统菜单","删除菜单", params vec![path_id("id")])}));

    paths.insert("/api/system/users".into(), json!({"get": op!("用户管理","用户分页列表", params pagination_params(vec![json!({"name":"username","in":"query","description":"按用户名模糊查询","schema":{"type":"string"}}),json!({"name":"phone","in":"query","description":"按手机号模糊查询","schema":{"type":"string"}}),json!({"name":"status","in":"query","description":"状态：1 启用，0 禁用","schema":{"type":"integer"}})]), response page_of("SysUser")),"post": op!("用户管理","新增用户", body "UserCreateRequest")}));
    paths.insert("/api/system/users/{id}".into(), json!({"get": op!("用户管理","用户详情", params vec![path_id("id")], response schema_ref("SysUser")),"put": op!("用户管理","编辑用户", params vec![path_id("id")], body "UserUpdateRequest"),"delete": op!("用户管理","删除用户", params vec![path_id("id")])}));
    paths.insert("/api/system/users/{id}/reset-password".into(), json!({"put": op!("用户管理","重置密码", params vec![path_id("id")], body "ResetPasswordRequest")}));
    paths.insert("/api/system/users/{id}/status".into(), json!({"put": op!("用户管理","启用/禁用", params vec![path_id("id")], body "UserStatusRequest")}));

    paths.insert("/api/system/roles".into(), json!({"get": op!("角色管理","角色列表", response array_of("SysRole")),"post": op!("角色管理","新增角色", body "SysRoleRequest")}));
    paths.insert("/api/system/roles/{id}".into(), json!({"put": op!("角色管理","编辑角色", params vec![path_id("id")], body "SysRoleRequest"),"delete": op!("角色管理","删除角色", params vec![path_id("id")])}));
    paths.insert("/api/system/roles/{id}/menus".into(), json!({"put": op!("角色管理","角色授权菜单", params vec![path_id("id")], body "RoleMenuRequest")}));

    paths.insert("/api/jobs".into(), json!({"get": op!("职位管理","职位分页列表", params pagination_params(vec![json!({"name":"title","in":"query","description":"按职位名称模糊查询","schema":{"type":"string"}}),json!({"name":"department","in":"query","description":"按部门模糊查询","schema":{"type":"string"}}),json!({"name":"status","in":"query","description":"职位状态：DRAFT/OPEN/CLOSED","schema":{"type":"string"}}),json!({"name":"jobType","in":"query","description":"岗位类型：FULL_TIME/PART_TIME/INTERN","schema":{"type":"string"}}),json!({"name":"eduRequire","in":"query","description":"学历要求","schema":{"type":"string"}})]), response page_of("JobPosition")),"post": op!("职位管理","新建职位", body "JobPositionRequest")}));
    paths.insert("/api/jobs/{id}".into(), json!({"get": op!("职位管理","职位详情", params vec![path_id("id")], response schema_ref("JobPosition")),"put": op!("职位管理","编辑职位", params vec![path_id("id")], body "JobPositionRequest"),"delete": op!("职位管理","删除职位", params vec![path_id("id")])}));
    paths.insert("/api/jobs/{id}/publish".into(), json!({"put": op!("职位管理","发布职位", params vec![path_id("id")])}));
    paths.insert("/api/jobs/{id}/close".into(), json!({"put": op!("职位管理","关闭职位", params vec![path_id("id")])}));

    paths.insert("/api/resumes".into(), json!({"get": op!("简历管理","简历分页列表", params pagination_params(vec![json!({"name":"name","in":"query","description":"按候选人姓名模糊查询","schema":{"type":"string"}}),json!({"name":"phone","in":"query","description":"按手机号模糊查询","schema":{"type":"string"}}),json!({"name":"highestEdu","in":"query","description":"按最高学历筛选","schema":{"type":"string"}}),json!({"name":"source","in":"query","description":"按来源筛选，如 MANUAL/FILE","schema":{"type":"string"}}),json!({"name":"skill","in":"query","description":"按技能标签模糊查询","schema":{"type":"string"}})]), response page_of("ResumeMain")),"post": op!("简历管理","录入简历", body "ResumeRequest", response schema_ref("ResumeMain"))}));
    paths.insert("/api/resumes/upload".into(), json!({"post": op!("简历管理","上传简历文件")}));
    paths.insert("/api/resumes/export".into(), json!({"get": op!("简历管理","导出简历 CSV")}));
    paths.insert("/api/resumes/{id}".into(), json!({"get": op!("简历管理","简历详情", params vec![path_id("id")], response schema_ref("ResumeMain")),"put": op!("简历管理","编辑简历", params vec![path_id("id")], body "ResumeRequest"),"delete": op!("简历管理","删除简历", params vec![path_id("id")])}));
    paths.insert("/api/resumes/{id}/file".into(), json!({"get": op!("简历管理","下载简历文件", params vec![path_id("id")])}));

    paths.insert("/api/match/run".into(), json!({"post": op!("智能匹配","发起匹配", body "RunMatchRequest", response array_of("MatchResult"))}));
    paths.insert("/api/match/results/{position_id}".into(), json!({"get": op!("智能匹配","职位匹配结果", params vec![path_id("position_id")], response array_of("MatchResult"))}));
    paths.insert("/api/match/config".into(), json!({"get": op!("智能匹配","获取匹配规则配置", response schema_ref("MatchConfig")),"put": op!("智能匹配","保存匹配规则配置", body "MatchConfigRequest")}));

    paths.insert("/api/applications".into(), json!({"get": op!("投递记录","投递分页列表", params pagination_params(vec![json!({"name":"positionId","in":"query","description":"按职位 ID 筛选","schema":{"type":"integer"}}),json!({"name":"status","in":"query","description":"按投递状态筛选","schema":{"type":"string"}})]), response page_of("JobApplication")),"post": op!("投递记录","创建投递记录", body "ApplicationCreateRequest", response schema_ref("JobApplication"))}));
    paths.insert("/api/applications/{id}".into(), json!({"get": op!("投递记录","投递详情", params vec![path_id("id")], response schema_ref("JobApplication"))}));
    paths.insert("/api/applications/{id}/status".into(), json!({"put": op!("投递记录","变更投递状态", params vec![path_id("id")], body "ApplicationStatusRequest")}));
    paths.insert("/api/applications/{id}/logs".into(), json!({"get": op!("投递记录","操作日志", params vec![path_id("id")], response array_of("ApplicationLog"))}));

    paths.insert("/api/interviews".into(), json!({"get": op!("面试管理","面试分页列表", params pagination_params(vec![json!({"name":"interviewer","in":"query","description":"按面试官模糊查询","schema":{"type":"string"}})]), response page_of("InterviewRecord")),"post": op!("面试管理","新增面试安排", body "InterviewRequest", response schema_ref("InterviewRecord"))}));
    paths.insert("/api/interviews/{id}".into(), json!({"get": op!("面试管理","面试详情", params vec![path_id("id")], response schema_ref("InterviewRecord")),"put": op!("面试管理","修改面试", params vec![path_id("id")], body "InterviewRequest")}));
    paths.insert("/api/interviews/{id}/result".into(), json!({"put": op!("面试管理","填写面试评价与结果", params vec![path_id("id")], body "InterviewResultRequest")}));

    paths.insert("/api/stats/dashboard".into(), json!({"get": op!("统计报表","数据看板", response schema_ref("DashboardStats"))}));
    paths.insert("/api/stats/source".into(), json!({"get": op!("统计报表","来源统计", params vec![json!({"name":"startDate","in":"query","description":"开始日期，格式 yyyy-MM-dd","schema":{"type":"string"}}),json!({"name":"endDate","in":"query","description":"结束日期，格式 yyyy-MM-dd","schema":{"type":"string"}})], response schema_ref("SourceStats"))}));

    Value::Object(paths)
}
