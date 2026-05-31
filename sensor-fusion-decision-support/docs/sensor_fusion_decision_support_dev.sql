-- =============================================================
-- 跨场景影像传感数据融合与决策支持系统 数据库初始化脚本
-- 数据库: sensor_fusion_decision_support_dev
-- 版本: V1.0  日期: 2026-05-27
-- =============================================================

CREATE DATABASE IF NOT EXISTS sensor_fusion_decision_support_dev
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE sensor_fusion_decision_support_dev;

-- -------------------------------------------
-- 1. 用户表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS sys_user (
  id          BIGINT       NOT NULL COMMENT '用户ID（雪花）',
  username    VARCHAR(64)  NOT NULL COMMENT '登录账号',
  real_name   VARCHAR(64)  NOT NULL COMMENT '姓名',
  password    VARCHAR(128) NOT NULL COMMENT '密码（BCrypt）',
  phone       VARCHAR(20)           COMMENT '手机号',
  department  VARCHAR(100)          COMMENT '部门',
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
  is_deleted  TINYINT      NOT NULL DEFAULT 0 COMMENT '0-正常 1-删除',
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by  BIGINT                COMMENT '创建人ID',
  updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by  BIGINT                COMMENT '更新人ID',
  remark      VARCHAR(500)          COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- -------------------------------------------
-- 2. 角色表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS sys_role (
  id          BIGINT       NOT NULL,
  role_name   VARCHAR(64)  NOT NULL COMMENT '角色名称',
  role_code   VARCHAR(64)  NOT NULL COMMENT '角色标识',
  description VARCHAR(200)          COMMENT '描述',
  status      TINYINT      NOT NULL DEFAULT 1,
  is_deleted  TINYINT      NOT NULL DEFAULT 0,
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by  BIGINT,
  updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by  BIGINT,
  remark      VARCHAR(500),
  PRIMARY KEY (id),
  UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- -------------------------------------------
-- 3. 用户角色关联表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS sys_user_role (
  user_id BIGINT NOT NULL COMMENT '用户ID',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- -------------------------------------------
-- 4. 菜单表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS sys_menu (
  id          BIGINT       NOT NULL,
  parent_id   BIGINT       NOT NULL DEFAULT 0 COMMENT '父节点ID',
  menu_name   VARCHAR(64)  NOT NULL COMMENT '菜单名称',
  menu_type   CHAR(1)      NOT NULL COMMENT 'M目录 C菜单 F按钮',
  path        VARCHAR(200)          COMMENT '路由路径',
  component   VARCHAR(200)          COMMENT '组件路径',
  icon        VARCHAR(100)          COMMENT '图标',
  permission  VARCHAR(100)          COMMENT '权限标识',
  sort        INT          NOT NULL DEFAULT 0 COMMENT '排序',
  status      TINYINT      NOT NULL DEFAULT 1,
  is_deleted  TINYINT      NOT NULL DEFAULT 0,
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by  BIGINT,
  updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by  BIGINT,
  remark      VARCHAR(500),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- -------------------------------------------
-- 5. 角色菜单关联表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS sys_role_menu (
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- -------------------------------------------
-- 6. 数据源配置表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS datasource_config (
  id            BIGINT       NOT NULL,
  ds_code       VARCHAR(32)  NOT NULL COMMENT '数据源编号（系统生成）',
  ds_name       VARCHAR(100) NOT NULL COMMENT '数据源名称',
  scene_type    VARCHAR(20)  NOT NULL COMMENT 'QUALITY/STORAGE/LOGISTICS/SECURITY',
  ds_type       VARCHAR(30)  NOT NULL COMMENT 'DEVICE/FILE_SERVER/DATABASE/OBJECT_STORAGE',
  conn_host     VARCHAR(200) NOT NULL COMMENT '连接地址',
  conn_port     INT          NOT NULL COMMENT '端口',
  auth_type     VARCHAR(20)  NOT NULL DEFAULT 'NONE' COMMENT 'NONE/PASSWORD/KEY',
  auth_config   TEXT                  COMMENT '认证配置（AES-256加密JSON）',
  field_mapping TEXT                  COMMENT '字段映射配置（JSON）',
  status        TINYINT      NOT NULL DEFAULT 1,
  is_deleted    TINYINT      NOT NULL DEFAULT 0,
  created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by    BIGINT,
  updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by    BIGINT,
  remark        VARCHAR(500),
  PRIMARY KEY (id),
  UNIQUE KEY uk_ds_code (ds_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源配置表';

-- -------------------------------------------
-- 7. 数据源状态表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS datasource_status (
  id            BIGINT      NOT NULL AUTO_INCREMENT,
  ds_id         BIGINT      NOT NULL COMMENT '数据源ID',
  conn_status   TINYINT     NOT NULL DEFAULT 1 COMMENT '0-异常 1-正常',
  error_msg     VARCHAR(500)         COMMENT '异常描述',
  last_data_time DATETIME            COMMENT '最近一次数据更新时间',
  checked_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
  PRIMARY KEY (id),
  KEY idx_ds_id (ds_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源状态记录表';

-- -------------------------------------------
-- 8. 融合方案表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS fusion_scheme (
  id           BIGINT       NOT NULL,
  scheme_code  VARCHAR(32)  NOT NULL COMMENT '方案编号（系统生成）',
  scheme_name  VARCHAR(100) NOT NULL COMMENT '方案名称',
  scene_types  JSON                  COMMENT '参与场景列表（JSON数组）',
  fusion_goal  VARCHAR(500)          COMMENT '融合目标描述',
  status       TINYINT      NOT NULL DEFAULT 1,
  is_deleted   TINYINT      NOT NULL DEFAULT 0,
  created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by   BIGINT,
  updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by   BIGINT,
  remark       VARCHAR(500),
  PRIMARY KEY (id),
  UNIQUE KEY uk_scheme_code (scheme_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='融合方案表';

-- -------------------------------------------
-- 9. 融合规则表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS fusion_rule (
  id                BIGINT       NOT NULL,
  scheme_id         BIGINT       NOT NULL COMMENT '所属方案ID',
  rule_name         VARCHAR(100) NOT NULL COMMENT '规则名称',
  fusion_type       VARCHAR(20)  NOT NULL COMMENT 'WEIGHTED/VOTE/PRIORITY',
  fusion_fields     JSON                  COMMENT '融合字段列表（JSON）',
  trigger_condition TEXT                  COMMENT '触发条件表达式',
  sort              INT          NOT NULL DEFAULT 0 COMMENT '执行顺序',
  status            TINYINT      NOT NULL DEFAULT 1,
  is_deleted        TINYINT      NOT NULL DEFAULT 0,
  created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by        BIGINT,
  updated_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by        BIGINT,
  remark            VARCHAR(500),
  PRIMARY KEY (id),
  KEY idx_scheme_id (scheme_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='融合规则表';

-- -------------------------------------------
-- 10. 融合权重表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS fusion_weight (
  id             BIGINT         NOT NULL,
  scheme_id      BIGINT         NOT NULL COMMENT '所属方案ID',
  rule_id        BIGINT         NOT NULL COMMENT '所属规则ID',
  ds_id          BIGINT         NOT NULL COMMENT '数据源ID',
  weight_value   DECIMAL(5,2)   NOT NULL COMMENT '权重值(0.00~1.00)',
  adjust_reason  VARCHAR(200)            COMMENT '调整原因',
  updated_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by     BIGINT,
  PRIMARY KEY (id),
  KEY idx_scheme_rule (scheme_id, rule_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='融合权重配置表';

-- -------------------------------------------
-- 11. 决策规则表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS decision_rule (
  id                BIGINT       NOT NULL,
  rule_code         VARCHAR(32)  NOT NULL COMMENT '规则编号（系统生成）',
  rule_name         VARCHAR(100) NOT NULL COMMENT '规则名称',
  scheme_id         BIGINT       NOT NULL COMMENT '关联融合方案ID',
  trigger_condition TEXT                  COMMENT '触发条件表达式（JSON）',
  decision_output   TEXT         NOT NULL COMMENT '决策输出内容',
  priority          INT          NOT NULL DEFAULT 100 COMMENT '优先级（数值越小越高）',
  status            TINYINT      NOT NULL DEFAULT 0 COMMENT '0-停用 1-启用',
  is_deleted        TINYINT      NOT NULL DEFAULT 0,
  created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by        BIGINT,
  updated_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by        BIGINT,
  remark            VARCHAR(500),
  PRIMARY KEY (id),
  UNIQUE KEY uk_rule_code (rule_code),
  KEY idx_scheme_id (scheme_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='决策规则表';

-- -------------------------------------------
-- 12. 决策条件表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS decision_condition (
  id               BIGINT       NOT NULL,
  condition_name   VARCHAR(100) NOT NULL COMMENT '条件名称',
  condition_field  VARCHAR(100) NOT NULL COMMENT '作用字段',
  operator         VARCHAR(20)  NOT NULL COMMENT 'GT/LT/EQ/BETWEEN/CONTAINS',
  threshold_value  VARCHAR(200) NOT NULL COMMENT '阈值',
  threshold_value2 VARCHAR(200)          COMMENT '阈值2（BETWEEN时使用）',
  description      VARCHAR(300)          COMMENT '条件用途说明',
  is_deleted       TINYINT      NOT NULL DEFAULT 0,
  created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by       BIGINT,
  updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by       BIGINT,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='决策条件表';

-- -------------------------------------------
-- 13. 融合结果表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS fusion_result (
  id                BIGINT       NOT NULL,
  result_code       VARCHAR(32)  NOT NULL COMMENT '结果编号（系统生成）',
  scheme_id         BIGINT       NOT NULL COMMENT '融合方案ID',
  scheme_name       VARCHAR(100)          COMMENT '融合方案名称（冗余）',
  scene_count       INT          NOT NULL DEFAULT 0 COMMENT '参与场景数',
  data_record_count INT          NOT NULL DEFAULT 0 COMMENT '融合数据条数',
  result_status     TINYINT      NOT NULL DEFAULT 1 COMMENT '0-异常 1-成功',
  error_msg         VARCHAR(500)          COMMENT '异常描述',
  raw_data_summary  JSON                  COMMENT '各场景原始数据摘要（JSON）',
  fusion_data       JSON                  COMMENT '融合后综合数据字段（JSON）',
  executed_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '融合执行时间',
  created_by        BIGINT                COMMENT '执行人/系统',
  PRIMARY KEY (id),
  UNIQUE KEY uk_result_code (result_code),
  KEY idx_scheme_id (scheme_id),
  KEY idx_executed_at (executed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='融合结果表';

-- -------------------------------------------
-- 14. 决策结果表
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS decision_result (
  id                BIGINT       NOT NULL,
  result_code       VARCHAR(32)  NOT NULL COMMENT '结果编号（系统生成）',
  fusion_result_id  BIGINT       NOT NULL COMMENT '关联融合结果ID',
  rule_id           BIGINT       NOT NULL COMMENT '触发的决策规则ID',
  rule_name         VARCHAR(100)          COMMENT '规则名称（冗余）',
  scheme_id         BIGINT                COMMENT '融合方案ID（冗余）',
  scheme_name       VARCHAR(100)          COMMENT '融合方案名称（冗余）',
  decision_output   TEXT         NOT NULL COMMENT '决策输出内容',
  triggered_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '触发时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_result_code (result_code),
  KEY idx_fusion_result_id (fusion_result_id),
  KEY idx_rule_id (rule_id),
  KEY idx_triggered_at (triggered_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='决策结果表';

-- =============================================================
-- 初始数据
-- =============================================================

-- 内置角色
INSERT INTO sys_role (id, role_name, role_code, description, status, is_deleted, created_at, created_by) VALUES
(1, '超级管理员',       'SUPER_ADMIN', '拥有全部权限',             1, 0, NOW(), 0),
(2, '融合配置工程师',    'FUSION_ENG',  '配置数据源、融合规则与决策规则', 1, 0, NOW(), 0),
(3, '决策分析员',       'ANALYST',     '查看融合/决策结果，导出报告',   1, 0, NOW(), 0),
(4, '只读用户',         'READONLY',    '只读查看融合结果与报表',       1, 0, NOW(), 0);

-- 内置超级管理员账号 (密码: Admin@123)
INSERT INTO sys_user (id, username, real_name, password, phone, department, status, is_deleted, created_at, created_by) VALUES
(1, 'admin', '系统管理员',
 '$2a$10$e.Xo8PiYQPT02kLJeLRGUO4WKqw8ta/lMMUqQbWhhuIvxcRjxP80O', -- Admin@123
 '13800000000', '信息技术部', 1, 0, NOW(), 0);

-- 用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 顶层菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, permission, sort, status, is_deleted, created_at, created_by) VALUES
(100, 0,   '基础管理',   'M', '/system',     NULL, 'setting',    NULL,                  1, 1, 0, NOW(), 0),
(200, 0,   '数据源管理', 'M', '/datasource', NULL, 'database',   NULL,                  2, 1, 0, NOW(), 0),
(300, 0,   '融合配置',   'M', '/fusion',     NULL, 'merge',      NULL,                  3, 1, 0, NOW(), 0),
(400, 0,   '决策规则',   'M', '/decision',   NULL, 'rule',       NULL,                  4, 1, 0, NOW(), 0),
(500, 0,   '融合结果',   'M', '/fusion-result', NULL, 'result',  NULL,                  5, 1, 0, NOW(), 0),
(600, 0,   '决策结果',   'M', '/decision-result', NULL, 'check', NULL,                  6, 1, 0, NOW(), 0),
(700, 0,   '报表分析',   'M', '/stats',      NULL, 'chart',      NULL,                  7, 1, 0, NOW(), 0);
