# 产线智能视觉缺陷分类与追溯管理系统 · 基础管理模块 PRD

> 所属系统：产线智能视觉缺陷分类与追溯管理系统
> 文档版本：V1.0 | 创建日期：2026-05-26

---

## 一、模块概述

基础管理模块为整个平台提供账号体系与权限控制基础，包括用户管理、角色管理、权限管理、菜单管理四个子功能，采用 RBAC（基于角色的访问控制）模型。

---

## 二、功能清单

| 功能点 | 优先级 | 说明 | 涉及角色 |
|--------|--------|------|----------|
| 用户列表查询 | P0 | 分页查询，支持姓名/账号/状态筛选 | 超级管理员 |
| 新增/编辑用户 | P0 | 维护用户基本信息与角色绑定 | 超级管理员 |
| 禁用/启用用户 | P0 | 切换账号状态 | 超级管理员 |
| 重置密码 | P0 | 重置为初始密码 | 超级管理员 |
| 删除用户 | P0 | 逻辑删除 | 超级管理员 |
| 角色列表查询 | P0 | 展示角色及关联用户数 | 超级管理员 |
| 新增/编辑角色 | P0 | 维护角色信息 | 超级管理员 |
| 角色授权 | P0 | 为角色分配菜单与按钮权限 | 超级管理员 |
| 菜单树管理 | P0 | 维护导航菜单节点 | 超级管理员 |

---

## 三、用户管理

### 3.1 字段说明

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|------|----------|------|
| username | string | 是 | 6~20位字母数字，全局唯一 | 登录账号，创建后不可修改 |
| realName | string | 是 | 2~20位 | 真实姓名 |
| phone | string | 是 | 11位手机号格式 | 手机号 |
| department | string | 否 | 不超过50字符 | 所属部门 |
| password | string | 是 | 8~20位，含大小写字母+数字 | 初始密码 |
| roleIds | array | 是 | 至少绑定1个角色 | 关联角色ID列表 |
| status | int | 是 | 0-禁用 1-启用 | 账号状态 |

### 3.2 接口

| 接口名称 | 方法 | 路径 | 权限标识 |
|----------|------|------|----------|
| 查询用户列表 | GET | /api/v1/system/users | system:user:list |
| 新增用户 | POST | /api/v1/system/users | system:user:add |
| 编辑用户 | PUT | /api/v1/system/users/{id} | system:user:edit |
| 禁用/启用 | PATCH | /api/v1/system/users/{id}/status | system:user:edit |
| 重置密码 | POST | /api/v1/system/users/{id}/reset-password | system:user:edit |
| 删除用户 | DELETE | /api/v1/system/users/{id} | system:user:delete |

#### 新增用户 Request

```json
POST /api/v1/system/users
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "zhangsan",
  "realName": "张三",
  "phone": "13800138001",
  "department": "质检部",
  "password": "Init@123",
  "roleIds": [2],
  "status": 1
}
```

---

## 四、角色管理

### 4.1 内置角色

| 角色名称 | 标识 | 权限范围 |
|---------|------|---------|
| 超级管理员 | SUPER_ADMIN | 全部权限，不可删除 |
| 质量工程师 | QUALITY_ENG | 配置缺陷分类、查看全产线数据 |
| 产线操作员 | LINE_OPERATOR | 仅查看本产线相关数据 |
| 管理人员 | MANAGER | 只读，可查看统计看板与导出报告 |

### 4.2 接口

| 接口名称 | 方法 | 路径 | 权限标识 |
|----------|------|------|----------|
| 查询角色列表 | GET | /api/v1/system/roles | system:role:list |
| 新增角色 | POST | /api/v1/system/roles | system:role:add |
| 编辑角色 | PUT | /api/v1/system/roles/{id} | system:role:edit |
| 角色授权 | PUT | /api/v1/system/roles/{id}/menus | system:role:edit |
| 删除角色 | DELETE | /api/v1/system/roles/{id} | system:role:delete |

---

## 五、权限管理

采用 RBAC 模型，权限粒度分三级：

| 权限层级 | 说明 |
|---------|------|
| 菜单权限 | 控制导航菜单的可见性 |
| 按钮权限 | 控制新增、编辑、删除、导出按钮的可见性 |
| 数据权限 | 产线操作员只能查看其归属产线的数据 |

---

## 六、菜单管理

### 6.1 节点类型

| 类型 | 说明 |
|------|------|
| 目录 | 一级导航分组，无路由 |
| 菜单 | 页面路由节点 |
| 按钮 | 页面内操作权限点 |

### 6.2 接口

| 接口名称 | 方法 | 路径 | 权限标识 |
|----------|------|------|----------|
| 查询菜单树 | GET | /api/v1/system/menus/tree | system:menu:list |
| 新增菜单 | POST | /api/v1/system/menus | system:menu:add |
| 编辑菜单 | PUT | /api/v1/system/menus/{id} | system:menu:edit |
| 删除菜单 | DELETE | /api/v1/system/menus/{id} | system:menu:delete |

---

## 七、登录认证

```
POST /api/v1/auth/login
Content-Type: application/json

Request Body:
{
  "username": "admin",
  "password": "Admin@123",
  "captcha": "ab3x",
  "captchaKey": "uuid-key"
}

Response:
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGci...",
    "refreshToken": "eyJhbGci...",
    "expiresIn": 7200,
    "userInfo": {
      "userId": 1,
      "realName": "张管理",
      "roles": ["SUPER_ADMIN"]
    }
  }
}
```

---

## 八、验收标准

**AC-001 用户登录**
- Given：用户持有有效账号（status=1）和正确密码
- When：在登录页输入账号密码及验证码，点击登录
- Then：登录成功，跳转至首页，导航菜单根据角色权限渲染

**AC-002 登录失败锁定**
- Given：用户连续输错密码 5 次
- When：第 5 次提交
- Then：系统提示账号已锁定，请30分钟后重试

**AC-003 账号唯一性校验**
- Given：系统已存在账号 user001
- When：新增用户时输入相同账号 user001
- Then：表单提示该账号已存在，请更换，保存被阻止

**AC-004 数据权限隔离**
- Given：产线操作员绑定产线B
- When：尝试通过接口访问产线A的缺陷记录
- Then：接口返回 403，message=无权访问该产线数据
