-- =============================================
-- 视觉检测影像质量一致性管控系统 数据库初始化脚本
-- 数据库：vision_quality_consistency_control_dev
-- 创建时间：2026-05-27
-- =============================================

CREATE DATABASE IF NOT EXISTS vision_quality_consistency_control_dev
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE vision_quality_consistency_control_dev;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username    VARCHAR(50)  NOT NULL                COMMENT '登录账号',
    password    VARCHAR(100) NOT NULL                COMMENT '密码（BCrypt）',
    real_name   VARCHAR(50)  NOT NULL                COMMENT '真实姓名',
    phone       VARCHAR(20)  NOT NULL                COMMENT '手机号',
    dept        VARCHAR(100) NULL                    COMMENT '部门',
    status      TINYINT(1)   NOT NULL DEFAULT 1      COMMENT '0-禁用 1-启用',
    is_deleted  TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by  BIGINT       NULL                    COMMENT '创建人ID',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by  BIGINT       NULL                    COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name   VARCHAR(50)  NOT NULL                COMMENT '角色名称',
    role_code   VARCHAR(50)  NOT NULL                COMMENT '角色标识',
    description VARCHAR(200) NULL                    COMMENT '描述',
    status      TINYINT(1)   NOT NULL DEFAULT 1      COMMENT '0-禁用 1-启用',
    is_deleted  TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  BIGINT       NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by  BIGINT       NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    parent_id   BIGINT       NOT NULL DEFAULT 0      COMMENT '父节点ID，0表示根',
    menu_name   VARCHAR(100) NOT NULL                COMMENT '菜单名称',
    menu_type   TINYINT(1)   NOT NULL                COMMENT '0-目录 1-菜单 2-按钮',
    path        VARCHAR(200) NULL                    COMMENT '路由路径',
    permission  VARCHAR(200) NULL                    COMMENT '权限标识',
    icon        VARCHAR(100) NULL                    COMMENT '图标',
    sort        INT          NOT NULL DEFAULT 0      COMMENT '排序',
    status      TINYINT(1)   NOT NULL DEFAULT 1      COMMENT '0-禁用 1-启用',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 质量指标表
-- ----------------------------
DROP TABLE IF EXISTS quality_metric;
CREATE TABLE quality_metric (
    id           BIGINT         NOT NULL AUTO_INCREMENT COMMENT '指标ID',
    metric_code  VARCHAR(50)    NOT NULL                COMMENT '指标编号，QM-{序号}',
    metric_name  VARCHAR(100)   NOT NULL                COMMENT '指标名称',
    metric_type  TINYINT(1)     NOT NULL                COMMENT '0-数值型 1-等级型',
    unit         VARCHAR(50)    NULL                    COMMENT '计量单位',
    min_value    DECIMAL(10,4)  NULL                    COMMENT '数值型下限',
    max_value    DECIMAL(10,4)  NULL                    COMMENT '数值型上限',
    level_desc   TEXT           NULL                    COMMENT '等级描述JSON',
    importance   TINYINT(1)     NOT NULL DEFAULT 1      COMMENT '0-低 1-中 2-高',
    remark       VARCHAR(500)   NULL                    COMMENT '备注',
    status       TINYINT(1)     NOT NULL DEFAULT 1      COMMENT '0-停用 1-启用',
    is_deleted   TINYINT(1)     NOT NULL DEFAULT 0,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   BIGINT         NULL,
    updated_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by   BIGINT         NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_metric_code (metric_code),
    UNIQUE KEY uk_metric_name (metric_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质量指标表';

-- ----------------------------
-- 质量标准模板表
-- ----------------------------
DROP TABLE IF EXISTS quality_template;
CREATE TABLE quality_template (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    template_code    VARCHAR(50)  NOT NULL                COMMENT '模板编号，QT-{序号}',
    template_name    VARCHAR(100) NOT NULL                COMMENT '模板名称',
    applicable_scene VARCHAR(200) NULL                    COMMENT '适用场景',
    remark           VARCHAR(500) NULL                    COMMENT '备注',
    status           TINYINT(1)   NOT NULL DEFAULT 1      COMMENT '0-停用 1-启用',
    is_deleted       TINYINT(1)   NOT NULL DEFAULT 0,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       BIGINT       NULL,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       BIGINT       NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_template_code (template_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质量标准模板表';

-- ----------------------------
-- 模板指标关联表
-- ----------------------------
DROP TABLE IF EXISTS quality_template_metric;
CREATE TABLE quality_template_metric (
    template_id BIGINT NOT NULL COMMENT '模板ID',
    metric_id   BIGINT NOT NULL COMMENT '指标ID',
    PRIMARY KEY (template_id, metric_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板指标关联表';

-- ----------------------------
-- 检测任务表
-- ----------------------------
DROP TABLE IF EXISTS quality_task;
CREATE TABLE quality_task (
    id                 BIGINT         NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    task_code          VARCHAR(50)    NOT NULL                COMMENT '任务编号，TASK-{yyyyMMdd}-{序号}',
    task_name          VARCHAR(200)   NOT NULL                COMMENT '任务名称',
    detection_target   VARCHAR(200)   NOT NULL                COMMENT '检测对象描述',
    template_id        BIGINT         NOT NULL                COMMENT '关联标准模板ID',
    image_count        INT            NOT NULL DEFAULT 0      COMMENT '影像总数量',
    plan_execute_time  DATETIME       NULL                    COMMENT '计划执行时间',
    priority           TINYINT(1)     NOT NULL DEFAULT 1      COMMENT '0-低 1-中 2-高',
    remark             VARCHAR(500)   NULL                    COMMENT '备注',
    status             TINYINT(1)     NOT NULL DEFAULT 1      COMMENT '1-待执行 2-执行中 3-已完成 4-已取消',
    start_time         DATETIME       NULL                    COMMENT '实际开始时间',
    end_time           DATETIME       NULL                    COMMENT '实际结束时间',
    qualified_count    INT            NOT NULL DEFAULT 0      COMMENT '合格影像数',
    unqualified_count  INT            NOT NULL DEFAULT 0      COMMENT '不合格影像数',
    qualified_rate     DECIMAL(5,2)   NULL                    COMMENT '合格率',
    is_deleted         TINYINT(1)     NOT NULL DEFAULT 0,
    created_at         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         BIGINT         NULL,
    updated_at         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by         BIGINT         NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_task_code (task_code),
    INDEX idx_template_status (template_id, status),
    INDEX idx_target_status (detection_target(50), status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测任务表';

-- ----------------------------
-- 检测记录表
-- ----------------------------
DROP TABLE IF EXISTS quality_detection_record;
CREATE TABLE quality_detection_record (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '检测记录ID',
    task_id          BIGINT       NOT NULL                COMMENT '关联任务ID',
    image_id         VARCHAR(200) NOT NULL                COMMENT '影像文件名或编号',
    measured_values  JSON         NOT NULL                COMMENT '各指标实测值JSON',
    is_qualified     TINYINT(1)   NOT NULL                COMMENT '0-不合格 1-合格',
    exceeded_metrics JSON         NULL                    COMMENT '超标指标详情JSON',
    detected_at      DATETIME     NOT NULL                COMMENT '检测时间',
    PRIMARY KEY (id),
    INDEX idx_task_qualified (task_id, is_qualified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测记录表';

-- ----------------------------
-- 不合格品表
-- ----------------------------
DROP TABLE IF EXISTS quality_defect;
CREATE TABLE quality_defect (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '不合格品ID',
    defect_code      VARCHAR(50)  NOT NULL                COMMENT '记录编号，DEF-{yyyyMMdd}-{序号}',
    task_id          BIGINT       NOT NULL                COMMENT '关联任务ID',
    image_id         VARCHAR(200) NOT NULL                COMMENT '影像标识',
    exceeded_metrics JSON         NOT NULL                COMMENT '超标指标信息JSON',
    exceeded_values  JSON         NOT NULL                COMMENT '各超标指标实测值JSON',
    standard_ranges  JSON         NOT NULL                COMMENT '各指标标准范围快照JSON',
    found_at         DATETIME     NOT NULL                COMMENT '发现时间',
    dispose_status   TINYINT(1)   NOT NULL DEFAULT 1      COMMENT '1-待处置 2-处置中 3-已处置 4-已忽略',
    verify_status    TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '0-待验证 1-已验证合格 2-验证不合格',
    ignore_reason    VARCHAR(500) NULL                    COMMENT '忽略原因',
    is_deleted       TINYINT(1)   NOT NULL DEFAULT 0,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       BIGINT       NULL,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       BIGINT       NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_defect_code (defect_code),
    INDEX idx_task_dispose (task_id, dispose_status),
    INDEX idx_found_dispose (found_at, dispose_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='不合格品表';

-- ----------------------------
-- 不合格品处置记录表
-- ----------------------------
DROP TABLE IF EXISTS quality_defect_dispose;
CREATE TABLE quality_defect_dispose (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '处置记录ID',
    defect_id      BIGINT       NOT NULL                COMMENT '关联不合格品ID',
    dispose_plan   TINYINT(1)   NOT NULL                COMMENT '1-重新采集 2-参数调整 3-设备维护 4-接受',
    operator_id    BIGINT       NOT NULL                COMMENT '处置人ID',
    operate_at     DATETIME     NOT NULL                COMMENT '处置时间',
    result_desc    VARCHAR(500) NOT NULL                COMMENT '处置结果说明',
    verify_status  TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '0-待验证 1-已验证合格 2-验证不合格',
    verify_comment VARCHAR(500) NULL                    COMMENT '验证备注',
    verify_at      DATETIME     NULL                    COMMENT '验证时间',
    PRIMARY KEY (id),
    INDEX idx_defect_id (defect_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='不合格品处置记录表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
-- 管理员用户（密码: Admin@123）
INSERT INTO sys_user (username, password, real_name, phone, dept, status)
VALUES ('admin', '$2a$10$xs5dJ5Unjq/FiHEZIwa7u.tNkbREm/PJ6S8Ze.U4utqsetzmUfA7O', '系统管理员', '13800138000', '系统管理部', 1);

-- 角色
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('系统管理员', 'SUPER_ADMIN', '拥有全部权限', 1),
('质量标准工程师', 'QUALITY_ENG', '负责质量标准定义与检测任务配置', 1),
('检测操作员', 'OPERATOR', '负责执行检测任务，查看检测结果', 1),
('管理人员', 'MANAGER', '查看质量趋势统计，掌握整体管控状态', 1);

-- 绑定 admin 用户为 SUPER_ADMIN
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 菜单初始化
INSERT INTO sys_menu (parent_id, menu_name, menu_type, path, permission, icon, sort, status) VALUES
(0, '系统管理', 0, '/system', NULL, 'Setting', 1, 1),
(1, '用户管理', 1, '/system/users', 'system:user:list', 'User', 1, 1),
(1, '角色管理', 1, '/system/roles', 'system:role:list', 'UserFilled', 2, 1),
(1, '菜单管理', 1, '/system/menus', 'system:menu:list', 'Menu', 3, 1),
(0, '质量管理', 0, '/quality', NULL, 'DataAnalysis', 2, 1),
(5, '质量指标', 1, '/quality/metrics', 'quality:metric:list', 'DocumentChecked', 1, 1),
(5, '标准模板', 1, '/quality/templates', 'quality:template:list', 'Files', 2, 1),
(5, '检测任务', 1, '/quality/tasks', 'quality:task:list', 'Tickets', 3, 1),
(5, '一致性分析', 1, '/quality/analysis', 'quality:analysis:view', 'TrendCharts', 4, 1),
(5, '不合格品管理', 1, '/quality/defects', 'quality:defect:list', 'Warning', 5, 1),
(0, '统计报表', 0, '/stats', NULL, 'Histogram', 3, 1),
(11, '质量统计看板', 1, '/stats/dashboard', 'stats:dashboard:view', 'Odometer', 1, 1),
(11, '趋势报表', 1, '/stats/trend', 'stats:trend:view', 'TrendCharts', 2, 1);

-- 质量指标示例
INSERT INTO quality_metric (metric_code, metric_name, metric_type, unit, min_value, max_value, importance, status) VALUES
('QM-001', '图像亮度均值', 0, 'gray', 100.0000, 200.0000, 2, 1),
('QM-002', '图像对比度', 0, '%', 30.0000, 90.0000, 2, 1),
('QM-003', '图像分辨率DPI', 0, 'dpi', 300.0000, 600.0000, 1, 1),
('QM-004', '图像清晰度等级', 1, NULL, NULL, NULL, 1, 1);

-- 标准模板示例
INSERT INTO quality_template (template_code, template_name, applicable_scene, status) VALUES
('QT-001', '通用视觉检测标准', '通用生产线检测', 1),
('QT-002', '高精度精密件检测标准', '精密零部件检测', 1);

INSERT INTO quality_template_metric (template_id, metric_id) VALUES (1, 1),(1, 2),(1, 3),(2, 1),(2, 2),(2, 3),(2, 4);
