-- =====================================================
-- 多场景影像传感设备资源协同调度系统 - 数据库初始化脚本
-- DB: imaging_device_scheduler_dev
-- =====================================================

CREATE DATABASE IF NOT EXISTS imaging_device_scheduler_dev
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE imaging_device_scheduler_dev;

-- =====================================================
-- 系统用户表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(64)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT 'BCrypt密码',
    real_name   VARCHAR(64)  COMMENT '真实姓名',
    phone       VARCHAR(20)  COMMENT '手机号',
    email       VARCHAR(128) COMMENT '邮箱',
    dept        VARCHAR(128) COMMENT '所属部门',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '0-正常 1-删除',
    created_by  BIGINT       COMMENT '创建人ID',
    updated_by  BIGINT       COMMENT '修改人ID',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- =====================================================
-- 系统角色表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name   VARCHAR(64)  NOT NULL COMMENT '角色名称',
    role_code   VARCHAR(64)  NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '描述',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    sort_order  INT          DEFAULT 0,
    deleted     TINYINT      NOT NULL DEFAULT 0,
    created_by  BIGINT,
    updated_by  BIGINT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色';

-- =====================================================
-- 用户角色关联
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联';

-- =====================================================
-- 系统菜单/权限表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_menu (
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id  BIGINT       NOT NULL DEFAULT 0 COMMENT '父ID，0为根',
    menu_name  VARCHAR(64)  NOT NULL,
    menu_type  TINYINT      COMMENT '1-目录 2-菜单 3-按钮',
    path       VARCHAR(255) COMMENT '路由路径',
    component  VARCHAR(255) COMMENT '组件路径',
    icon       VARCHAR(64)  COMMENT '图标',
    perms      VARCHAR(255) COMMENT '权限标识',
    status     TINYINT      NOT NULL DEFAULT 1,
    sort_order INT          DEFAULT 0,
    deleted    TINYINT      NOT NULL DEFAULT 0,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单';

-- =====================================================
-- 角色菜单关联
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    UNIQUE KEY uk_role_menu (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联';

-- =====================================================
-- 场景分组表
-- =====================================================
CREATE TABLE IF NOT EXISTS scene_group (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_name  VARCHAR(128) NOT NULL,
    description VARCHAR(500),
    sort_order  INT          DEFAULT 0,
    deleted     TINYINT      NOT NULL DEFAULT 0,
    created_by  BIGINT,
    updated_by  BIGINT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景分组';

-- =====================================================
-- 场景信息表
-- =====================================================
CREATE TABLE IF NOT EXISTS scene_info (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    scene_name  VARCHAR(128) NOT NULL,
    scene_code  VARCHAR(64)  UNIQUE COMMENT '场景编码',
    group_id    BIGINT       COMMENT '所属分组',
    location    VARCHAR(255) COMMENT '位置描述',
    description VARCHAR(500),
    latitude    DECIMAL(10,7) COMMENT '纬度',
    longitude   DECIMAL(10,7) COMMENT '经度',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '0-停用 1-启用',
    deleted     TINYINT      NOT NULL DEFAULT 0,
    created_by  BIGINT,
    updated_by  BIGINT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景信息';

-- =====================================================
-- 设备信息表
-- =====================================================
CREATE TABLE IF NOT EXISTS device_info (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_name  VARCHAR(128) NOT NULL,
    device_code  VARCHAR(64)  UNIQUE COMMENT '设备编码',
    device_type  VARCHAR(64)  COMMENT '设备类型：CAMERA/SENSOR/RADAR等',
    scene_id     BIGINT       COMMENT '所属场景',
    ip_address   VARCHAR(64)  COMMENT 'IP地址',
    port         INT          COMMENT '端口',
    manufacturer VARCHAR(128) COMMENT '制造商',
    model        VARCHAR(128) COMMENT '型号',
    specs        TEXT         COMMENT '规格参数JSON',
    status       TINYINT      NOT NULL DEFAULT 3 COMMENT '1-在线 2-占用 3-离线 4-故障 5-维护',
    deleted      TINYINT      NOT NULL DEFAULT 0,
    created_by   BIGINT,
    updated_by   BIGINT,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息';

-- =====================================================
-- 设备参数表
-- =====================================================
CREATE TABLE IF NOT EXISTS device_param (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id   BIGINT       NOT NULL,
    param_key   VARCHAR(128) NOT NULL,
    param_value VARCHAR(1024),
    param_desc  VARCHAR(255),
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_device_param (device_id, param_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备参数';

-- =====================================================
-- 设备故障记录
-- =====================================================
CREATE TABLE IF NOT EXISTS device_fault_record (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id     BIGINT       NOT NULL,
    fault_type    VARCHAR(64)  COMMENT '故障类型',
    fault_desc    VARCHAR(500) COMMENT '故障描述',
    fault_time    DATETIME     COMMENT '故障时间',
    resolved_time DATETIME     COMMENT '解决时间',
    resolved_by   BIGINT       COMMENT '处理人ID',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备故障记录';

-- =====================================================
-- 任务信息表
-- =====================================================
CREATE TABLE IF NOT EXISTS task_info (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_name         VARCHAR(255) NOT NULL,
    task_code         VARCHAR(64)  UNIQUE,
    scene_id          BIGINT       COMMENT '关联场景',
    priority          INT          NOT NULL DEFAULT 5 COMMENT '优先级1-10',
    device_count      INT          NOT NULL DEFAULT 1 COMMENT '需要设备数',
    plan_start_time   DATETIME     COMMENT '计划开始',
    plan_end_time     DATETIME     COMMENT '计划结束',
    actual_start_time DATETIME     COMMENT '实际开始',
    actual_end_time   DATETIME     COMMENT '实际结束',
    description       VARCHAR(1000),
    status            INT          NOT NULL DEFAULT 10 COMMENT '10-待分配 20-已分配 30-执行中 50-已完成 -20-已取消',
    deleted           TINYINT      NOT NULL DEFAULT 0,
    created_by        BIGINT,
    updated_by        BIGINT,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务信息';

-- =====================================================
-- 任务设备关联
-- =====================================================
CREATE TABLE IF NOT EXISTS task_device_rel (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id     BIGINT NOT NULL,
    device_id   BIGINT NOT NULL,
    assign_time DATETIME COMMENT '分配时间',
    is_active   TINYINT NOT NULL DEFAULT 1 COMMENT '1-有效 0-已撤销',
    KEY idx_task_id (task_id),
    KEY idx_device_id (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务设备关联';

-- =====================================================
-- 调度配置表
-- =====================================================
CREATE TABLE IF NOT EXISTS dispatch_config (
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    max_devices_per_task     INT          NOT NULL DEFAULT 5 COMMENT '每任务最大设备数',
    task_timeout_minutes     INT          NOT NULL DEFAULT 120 COMMENT '任务超时分钟',
    auto_dispatch_enabled    TINYINT      NOT NULL DEFAULT 0 COMMENT '是否启用自动调度',
    dispatch_strategy        VARCHAR(64)  NOT NULL DEFAULT 'MANUAL' COMMENT '调度策略',
    alert_threshold_minutes  INT          NOT NULL DEFAULT 30 COMMENT '预警阈值分钟',
    updated_at               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调度配置';

-- =====================================================
-- 调度日志
-- =====================================================
CREATE TABLE IF NOT EXISTS dispatch_log (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id       BIGINT       COMMENT '任务ID',
    action        VARCHAR(64)  NOT NULL COMMENT '操作动作',
    action_desc   VARCHAR(500) COMMENT '操作描述',
    device_ids    VARCHAR(500) COMMENT '涉及设备IDs（逗号分隔）',
    operator_id   BIGINT       COMMENT '操作人ID',
    operator_name VARCHAR(64)  COMMENT '操作人姓名',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调度日志';

-- =====================================================
-- 初始数据
-- =====================================================

-- 超级管理员 (密码: Admin@123456 的BCrypt哈希)
INSERT IGNORE INTO sys_user(id, username, password, real_name, dept, status, created_at, updated_at)
VALUES(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', '技术部', 1, NOW(), NOW());

-- 4个角色
INSERT IGNORE INTO sys_role(id, role_name, role_code, description, status, sort_order, created_at, updated_at) VALUES
(1, '超级管理员', 'SUPER_ADMIN',   '拥有所有权限', 1, 1, NOW(), NOW()),
(2, '调度管理员', 'DISPATCH_ADMIN','负责任务调度', 1, 2, NOW(), NOW()),
(3, '设备运维',   'DEVICE_OPS',   '负责设备管理', 1, 3, NOW(), NOW()),
(4, '普通操作员', 'OPERATOR',     '只读查看权限', 1, 4, NOW(), NOW());

-- admin用户绑定超级管理员角色
INSERT IGNORE INTO sys_user_role(user_id, role_id) VALUES(1, 1);

-- 基础菜单权限（SUPER_ADMIN 拥有所有）
INSERT IGNORE INTO sys_menu(id, parent_id, menu_name, menu_type, perms, sort_order, status) VALUES
(1,  0, '系统管理', 1, NULL,                    1, 1),
(2,  1, '用户管理', 2, NULL,                    1, 1),
(3,  2, '查询用户', 3, 'system:user:list',       1, 1),
(4,  2, '新增用户', 3, 'system:user:add',        2, 1),
(5,  2, '编辑用户', 3, 'system:user:edit',       3, 1),
(6,  2, '删除用户', 3, 'system:user:delete',     4, 1),
(7,  1, '角色管理', 2, NULL,                    2, 1),
(8,  7, '查询角色', 3, 'system:role:list',       1, 1),
(9,  7, '新增角色', 3, 'system:role:add',        2, 1),
(10, 7, '编辑角色', 3, 'system:role:edit',       3, 1),
(11, 7, '删除角色', 3, 'system:role:delete',     4, 1),
(12, 0, '场景管理', 1, NULL,                    2, 1),
(13, 12,'场景列表', 2, NULL,                    1, 1),
(14, 13,'查询场景', 3, 'scene:info:list',        1, 1),
(15, 13,'新增场景', 3, 'scene:info:add',         2, 1),
(16, 13,'编辑场景', 3, 'scene:info:edit',        3, 1),
(17, 13,'删除场景', 3, 'scene:info:delete',      4, 1),
(18, 0, '设备管理', 1, NULL,                    3, 1),
(19, 18,'设备列表', 2, NULL,                    1, 1),
(20, 19,'查询设备', 3, 'device:info:list',       1, 1),
(21, 19,'新增设备', 3, 'device:info:add',        2, 1),
(22, 19,'编辑设备', 3, 'device:info:edit',       3, 1),
(23, 19,'删除设备', 3, 'device:info:delete',     4, 1),
(24, 0, '任务调度', 1, NULL,                    4, 1),
(25, 24,'任务列表', 2, NULL,                    1, 1),
(26, 25,'查询任务', 3, 'task:info:list',         1, 1),
(27, 25,'新增任务', 3, 'task:info:add',          2, 1),
(28, 25,'调度操作', 3, 'task:info:dispatch',     3, 1),
(29, 25,'取消任务', 3, 'task:info:cancel',       4, 1),
(30, 25,'删除任务', 3, 'task:info:delete',       5, 1),
(31, 24,'调度总览', 2, 'dispatch:overview:view', 2, 1),
(32, 24,'甘特图',   2, 'dispatch:gantt:view',    3, 1),
(33, 0, '统计分析', 1, NULL,                    5, 1),
(34, 33,'统计查看', 2, 'statistics:view',        1, 1);

-- SUPER_ADMIN 拥有所有菜单权限
INSERT IGNORE INTO sys_role_menu(role_id, menu_id)
SELECT 1, id FROM sys_menu WHERE deleted = 0;

-- 默认调度配置（只有一条）
INSERT IGNORE INTO dispatch_config(id, max_devices_per_task, task_timeout_minutes, auto_dispatch_enabled, dispatch_strategy, alert_threshold_minutes)
VALUES(1, 5, 120, 0, 'MANUAL', 30);

