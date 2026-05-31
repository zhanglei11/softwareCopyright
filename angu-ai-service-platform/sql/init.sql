-- ============================================================
-- 安谷AI多场景服务平台 数据库初始化脚本
-- DB: angu_ai_service_platform_dev
-- ============================================================
CREATE DATABASE IF NOT EXISTS angu_ai_service_platform_dev DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE angu_ai_service_platform_dev;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(64)  NOT NULL UNIQUE COMMENT '登录账号',
  real_name     VARCHAR(64)  COMMENT '姓名',
  phone         VARCHAR(20)  COMMENT '手机号',
  email         VARCHAR(128) COMMENT '邮箱',
  department    VARCHAR(128) COMMENT '部门',
  password      VARCHAR(255) NOT NULL COMMENT 'BCrypt密码',
  daily_limit   INT          DEFAULT 100 COMMENT '每日调用上限',
  status        TINYINT      DEFAULT 1 COMMENT '0禁用 1启用',
  error_count   INT          DEFAULT 0 COMMENT '密码错误次数',
  locked_until  DATETIME     COMMENT '锁定截止时间',
  deleted       TINYINT      DEFAULT 0 COMMENT '0正常 1删除',
  created_time  DATETIME     DEFAULT CURRENT_TIMESTAMP,
  updated_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name   VARCHAR(64)  NOT NULL COMMENT '角色名',
  role_code   VARCHAR(64)  NOT NULL UNIQUE COMMENT '角色编码',
  builtin     TINYINT      DEFAULT 0 COMMENT '是否内置',
  status      TINYINT      DEFAULT 1,
  description VARCHAR(255),
  created_time DATETIME    DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色';

-- 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  menu_name   VARCHAR(64)  NOT NULL,
  parent_id   BIGINT       DEFAULT 0,
  menu_type   CHAR(1)      DEFAULT 'M' COMMENT 'D目录 M菜单 B按钮',
  path        VARCHAR(255),
  component   VARCHAR(255),
  perms       VARCHAR(255) COMMENT '权限标识',
  icon        VARCHAR(100),
  sort        INT          DEFAULT 0,
  visible     TINYINT      DEFAULT 1,
  status      TINYINT      DEFAULT 1,
  created_time DATETIME    DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限';

-- 用户角色关联
CREATE TABLE IF NOT EXISTS sys_user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 角色菜单关联
CREATE TABLE IF NOT EXISTS sys_role_menu (
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 角色场景关联
CREATE TABLE IF NOT EXISTS sys_role_scene (
  role_id  BIGINT NOT NULL,
  scene_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, scene_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 场景分类
CREATE TABLE IF NOT EXISTS ai_scene_category (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  name         VARCHAR(64)  NOT NULL,
  icon         VARCHAR(100),
  sort         INT          DEFAULT 0,
  deleted      TINYINT      DEFAULT 0,
  created_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景分类';

-- AI模型配置
CREATE TABLE IF NOT EXISTS ai_model_config (
  id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
  model_name          VARCHAR(64)  NOT NULL,
  model_id            VARCHAR(128) NOT NULL COMMENT '模型ID/编码',
  provider            VARCHAR(64)  COMMENT '提供商',
  api_url             VARCHAR(512),
  api_key_encrypted   VARCHAR(512) COMMENT 'Base64编码的API Key',
  max_context_tokens  INT          DEFAULT 4096,
  status              TINYINT      DEFAULT 1,
  remark              VARCHAR(512),
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI模型配置';

-- AI场景
CREATE TABLE IF NOT EXISTS ai_scene (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  name             VARCHAR(128) NOT NULL,
  category_id      BIGINT,
  icon             VARCHAR(100),
  description      TEXT,
  usage_guide      TEXT,
  model_id         BIGINT,
  kb_id            BIGINT,
  system_prompt    TEXT,
  user_prompt_tpl  TEXT,
  input_variables  TEXT COMMENT 'JSON格式变量定义',
  max_tokens       INT          DEFAULT 2048,
  temperature      DECIMAL(3,2) DEFAULT 0.70,
  multi_turn       TINYINT      DEFAULT 1,
  status           VARCHAR(16)  DEFAULT 'DRAFT' COMMENT 'DRAFT/ONLINE/OFFLINE',
  deleted          TINYINT      DEFAULT 0,
  creator_id       BIGINT,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI场景';

-- 对话
CREATE TABLE IF NOT EXISTS ai_conversation (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id      BIGINT       NOT NULL,
  scene_id     BIGINT       NOT NULL,
  title        VARCHAR(255) DEFAULT '新对话',
  deleted      TINYINT      DEFAULT 0,
  created_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话';

-- 消息
CREATE TABLE IF NOT EXISTS ai_message (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  conversation_id  BIGINT       NOT NULL,
  role             VARCHAR(16)  NOT NULL COMMENT 'USER/ASSISTANT/SYSTEM',
  content          MEDIUMTEXT,
  token_count      INT          DEFAULT 0,
  created_time     DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话消息';

-- 调用日志
CREATE TABLE IF NOT EXISTS ai_call_log (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id            BIGINT,
  scene_id           BIGINT,
  model_id           BIGINT,
  conversation_id    BIGINT,
  prompt_tokens      INT     DEFAULT 0,
  completion_tokens  INT     DEFAULT 0,
  total_tokens       INT     DEFAULT 0,
  latency_ms         BIGINT  DEFAULT 0,
  success            TINYINT DEFAULT 1,
  error_msg          VARCHAR(512),
  call_time          DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI调用日志';

-- 知识库
CREATE TABLE IF NOT EXISTS kb_knowledge_base (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  name         VARCHAR(128) NOT NULL,
  description  VARCHAR(512),
  doc_count    INT          DEFAULT 0,
  deleted      TINYINT      DEFAULT 0,
  creator_id   BIGINT,
  created_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库';

-- 知识库文档
CREATE TABLE IF NOT EXISTS kb_document (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  kb_id        BIGINT       NOT NULL,
  file_name    VARCHAR(255),
  file_path    VARCHAR(512),
  file_size    BIGINT,
  file_type    VARCHAR(32),
  parse_status VARCHAR(16)  DEFAULT 'PENDING' COMMENT 'PENDING/PROCESSING/DONE/FAILED',
  chunk_count  INT          DEFAULT 0,
  error_msg    VARCHAR(512),
  deleted      TINYINT      DEFAULT 0,
  created_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库文档';

-- 用户场景收藏
CREATE TABLE IF NOT EXISTS user_scene_favorite (
  user_id      BIGINT   NOT NULL,
  scene_id     BIGINT   NOT NULL,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, scene_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景收藏';

-- ============================================================
-- 初始数据
-- ============================================================
INSERT IGNORE INTO sys_role(role_name, role_code, builtin, status, description) VALUES
  ('超级管理员', 'SUPER_ADMIN', 1, 1, '系统内置超级管理员'),
  ('AI管理员',   'AI_ADMIN',   1, 1, '系统内置AI管理员'),
  ('普通用户',   'NORMAL_USER',1, 1, '系统内置普通用户');

-- 默认管理员账号 admin / Angu@2024 (BCrypt)
INSERT IGNORE INTO sys_user(username, real_name, password, status, daily_limit) VALUES
  ('admin', '超级管理员', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, 9999);

INSERT IGNORE INTO sys_user_role(user_id, role_id)
  SELECT u.id, r.id FROM sys_user u, sys_role r
  WHERE u.username='admin' AND r.role_code='SUPER_ADMIN';

INSERT IGNORE INTO ai_scene_category(name, icon, sort) VALUES
  ('文本创作', 'edit', 1),
  ('代码助手', 'code', 2),
  ('数据分析', 'bar-chart', 3),
  ('客服问答', 'customer-service', 4);
