-- ================================================
-- 产线智能视觉缺陷分类与追溯管理系统  初始化 DDL
-- 数据库: vision_defect_traceability_dev
-- ================================================
CREATE DATABASE IF NOT EXISTS vision_defect_traceability_dev
  DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE vision_defect_traceability_dev;

DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS alert_record;
DROP TABLE IF EXISTS alert_rule;
DROP TABLE IF EXISTS defect_image;
DROP TABLE IF EXISTS defect_record;
DROP TABLE IF EXISTS defect_category;
DROP TABLE IF EXISTS product_type;
DROP TABLE IF EXISTS line_info;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  username   VARCHAR(50)  NOT NULL UNIQUE,
  real_name  VARCHAR(50),
  password   VARCHAR(255) NOT NULL,
  phone      VARCHAR(20),
  email      VARCHAR(100),
  status     TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_role (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name  VARCHAR(50)  NOT NULL,
  role_key   VARCHAR(50)  NOT NULL UNIQUE,
  status     TINYINT NOT NULL DEFAULT 1,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_menu (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  parent_id  BIGINT NOT NULL DEFAULT 0,
  menu_name  VARCHAR(80)  NOT NULL,
  menu_type  CHAR(1) NOT NULL COMMENT 'M目录 C菜单 F按钮',
  perm       VARCHAR(100) COMMENT '权限标识',
  path       VARCHAR(200),
  icon       VARCHAR(100),
  sort_order INT NOT NULL DEFAULT 0,
  status     TINYINT NOT NULL DEFAULT 1,
  is_deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY(user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_role_menu (
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL,
  PRIMARY KEY(role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE line_info (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  line_no     VARCHAR(50)  NOT NULL UNIQUE COMMENT '产线编号',
  line_name   VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  status      TINYINT NOT NULL DEFAULT 1,
  is_deleted  TINYINT NOT NULL DEFAULT 0,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product_type (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  line_id     BIGINT NOT NULL,
  type_name   VARCHAR(100) NOT NULL,
  type_code   VARCHAR(50),
  spec        VARCHAR(255),
  status      TINYINT NOT NULL DEFAULT 1,
  is_deleted  TINYINT NOT NULL DEFAULT 0,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE defect_category (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  code        VARCHAR(50)  NOT NULL UNIQUE,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  status      TINYINT NOT NULL DEFAULT 1,
  is_deleted  TINYINT NOT NULL DEFAULT 0,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE defect_record (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  serial_no      VARCHAR(100) NOT NULL COMMENT '序列号',
  batch_no       VARCHAR(100) NOT NULL COMMENT '批次号',
  line_id        BIGINT NOT NULL,
  product_id     BIGINT NOT NULL,
  category_id    BIGINT,
  level          TINYINT COMMENT '1轻微 2一般 3严重',
  result         TINYINT NOT NULL COMMENT '0缺陷 1合格',
  shift          VARCHAR(10) COMMENT 'A/B/C',
  detect_time    DATETIME NOT NULL,
  dispose_status TINYINT NOT NULL DEFAULT 0 COMMENT '0待处理 1已处理',
  dispose_remark VARCHAR(500),
  dispose_by     VARCHAR(50),
  dispose_at     DATETIME,
  is_deleted     TINYINT NOT NULL DEFAULT 0,
  created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_batch(batch_no),
  INDEX idx_serial(serial_no),
  INDEX idx_time(detect_time),
  INDEX idx_line(line_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE defect_image (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  record_id   BIGINT NOT NULL,
  image_url   VARCHAR(500) NOT NULL,
  annotations TEXT,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_record(record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE alert_rule (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  rule_name       VARCHAR(100) NOT NULL,
  line_id         BIGINT,
  condition_type  VARCHAR(50)  NOT NULL COMMENT 'DEFECT_RATE/DEFECT_COUNT/LEVEL',
  threshold       DECIMAL(10,4) NOT NULL,
  stat_cycle      INT NOT NULL DEFAULT 60 COMMENT '统计周期(分钟)',
  alert_level     TINYINT NOT NULL DEFAULT 1 COMMENT '1普通 2重要 3紧急',
  notify_user_ids TEXT,
  status          TINYINT NOT NULL DEFAULT 1,
  is_deleted      TINYINT NOT NULL DEFAULT 0,
  created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
  created_by      VARCHAR(50),
  updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by      VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE alert_record (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  rule_id       BIGINT NOT NULL,
  alert_content TEXT  NOT NULL,
  alert_time    DATETIME NOT NULL,
  handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '0未处理 1已处理',
  handle_remark VARCHAR(500),
  handle_by     VARCHAR(50),
  handle_at     DATETIME,
  created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_rule(rule_id),
  INDEX idx_time(alert_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==================== 初始数据 ====================

-- 超级管理员  密码: Admin@123  (BCrypt)
INSERT INTO sys_user(id,username,real_name,password,status)
VALUES(1,'admin','超级管理员','$2b$10$ek2mY8wW2eL0YEiw3qbLbO5Ph68XtAmqSvI8LDJ/MOi6zP3a4DnxW',1);

INSERT INTO sys_role(id,role_name,role_key,status) VALUES
(1,'超级管理员','super_admin',1),
(2,'质检员','quality_inspector',1),
(3,'产线主管','line_supervisor',1),
(4,'只读查看','viewer',1);

INSERT INTO sys_user_role VALUES(1,1);

INSERT INTO defect_category(code,name,status) VALUES
('DEF-SCRATCH','划痕',1),
('DEF-DENT','凹坑',1),
('DEF-CRACK','裂纹',1),
('DEF-STAIN','污渍',1),
('DEF-BUBBLE','气泡',1),
('DEF-MISSING','缺料',1);

INSERT INTO line_info(id,line_no,line_name,status) VALUES
(1,'LINE-001','A1号产线',1),
(2,'LINE-002','A2号产线',1),
(3,'LINE-003','B1号产线',1);
