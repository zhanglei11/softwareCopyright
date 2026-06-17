-- ============================================================
-- 智能视觉影像识别辅助分析系统 数据库初始化脚本
-- 数据库: smart_vision_analysis_dev
-- 字符集: utf8mb4
-- ============================================================

CREATE DATABASE IF NOT EXISTS `smart_vision_analysis_dev`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `smart_vision_analysis_dev`;

-- ----------------------------
-- 系统用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`            BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`      VARCHAR(64) NOT NULL COMMENT '用户名',
  `password`      VARCHAR(255) NOT NULL COMMENT '密码(BCrypt)',
  `real_name`     VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
  `phone`         VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `department`    VARCHAR(64) DEFAULT NULL COMMENT '部门',
  `status`        TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 0禁用',
  `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `created_by`    BIGINT DEFAULT NULL COMMENT '创建人',
  `created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`       TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- 系统角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name`   VARCHAR(64) NOT NULL COMMENT '角色名称',
  `role_code`   VARCHAR(64) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
  `status`      TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 0禁用',
  `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id`   BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID(0=根)',
  `menu_name`   VARCHAR(64) NOT NULL COMMENT '菜单名称',
  `menu_type`   CHAR(1) NOT NULL DEFAULT 'M' COMMENT '类型:M目录 C菜单 F按钮',
  `path`        VARCHAR(255) DEFAULT NULL COMMENT '路由路径',
  `component`   VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
  `perms`       VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
  `icon`        VARCHAR(64) DEFAULT NULL COMMENT '图标',
  `sort_order`  INT NOT NULL DEFAULT 0 COMMENT '排序',
  `visible`     TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示:1显示 0隐藏',
  `status`      TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id`              BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id`         BIGINT DEFAULT NULL COMMENT '用户ID',
  `username`        VARCHAR(64) DEFAULT NULL COMMENT '用户名',
  `module`          VARCHAR(64) DEFAULT NULL COMMENT '模块名',
  `operation`       VARCHAR(128) DEFAULT NULL COMMENT '操作描述',
  `request_method`  VARCHAR(16) DEFAULT NULL COMMENT '请求方法',
  `request_url`     VARCHAR(512) DEFAULT NULL COMMENT '请求地址',
  `request_params`  TEXT COMMENT '请求参数',
  `response_result` TEXT COMMENT '返回结果',
  `status`          TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1成功 0失败',
  `ip`              VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
  `created_at`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- 图像分类表
-- ----------------------------
DROP TABLE IF EXISTS `image_category`;
CREATE TABLE `image_category` (
  `id`            BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id`     BIGINT NOT NULL DEFAULT 0 COMMENT '父分类ID(0=根)',
  `sort_order`    INT NOT NULL DEFAULT 0 COMMENT '排序',
  `created_by`    BIGINT DEFAULT NULL COMMENT '创建人',
  `created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted`       TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图像分类表';

-- ----------------------------
-- 图像文件表
-- ----------------------------
DROP TABLE IF EXISTS `image_file`;
CREATE TABLE `image_file` (
  `id`                  BIGINT NOT NULL AUTO_INCREMENT COMMENT '图像ID',
  `image_no`            VARCHAR(64) NOT NULL COMMENT '图像编号',
  `file_name`           VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `file_path`           VARCHAR(512) NOT NULL COMMENT '存储路径',
  `file_format`         VARCHAR(16) DEFAULT NULL COMMENT '文件格式:jpg/png/bmp',
  `file_size`           BIGINT DEFAULT NULL COMMENT '文件大小(Byte)',
  `category_id`         BIGINT DEFAULT NULL COMMENT '所属分类ID',
  `recognition_status`  TINYINT NOT NULL DEFAULT 0 COMMENT '识别状态:0未识别 1识别中 2已完成 3识别失败',
  `remark`              VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `uploaded_by`         BIGINT DEFAULT NULL COMMENT '上传人',
  `uploaded_at`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `deleted`             TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_image_no` (`image_no`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_uploaded_at` (`uploaded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图像文件表';

-- ----------------------------
-- 模型版本表
-- ----------------------------
DROP TABLE IF EXISTS `model_version`;
CREATE TABLE `model_version` (
  `id`             BIGINT NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `model_name`     VARCHAR(128) NOT NULL COMMENT '模型名称',
  `version_no`     VARCHAR(64) NOT NULL COMMENT '版本号',
  `scene_desc`     VARCHAR(255) DEFAULT NULL COMMENT '适用场景描述',
  `support_labels` TEXT COMMENT '支持的标签(逗号分隔)',
  `release_date`   DATE DEFAULT NULL COMMENT '发布日期',
  `status`         TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1可用 0废弃',
  `remark`         VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `created_by`     BIGINT DEFAULT NULL COMMENT '创建人',
  `created_at`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`        TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_version` (`model_name`, `version_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='算法模型版本表';

-- ----------------------------
-- 识别任务表
-- ----------------------------
DROP TABLE IF EXISTS `recognition_task`;
CREATE TABLE `recognition_task` (
  `id`                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_no`              VARCHAR(64) NOT NULL COMMENT '任务编号',
  `task_name`            VARCHAR(128) NOT NULL COMMENT '任务名称',
  `model_version_id`     BIGINT NOT NULL COMMENT '模型版本ID',
  `model_version_no`     VARCHAR(64) DEFAULT NULL COMMENT '模型版本号(冗余)',
  `confidence_threshold` DECIMAL(5,4) NOT NULL DEFAULT 0.5000 COMMENT '置信度阈值',
  `select_mode`          TINYINT NOT NULL DEFAULT 0 COMMENT '选图模式:0按分类 1手动选择',
  `total_count`          INT NOT NULL DEFAULT 0 COMMENT '总图像数',
  `processed_count`      INT NOT NULL DEFAULT 0 COMMENT '已处理数',
  `success_count`        INT NOT NULL DEFAULT 0 COMMENT '成功数',
  `fail_count`           INT NOT NULL DEFAULT 0 COMMENT '失败数',
  `avg_confidence`       DECIMAL(5,4) DEFAULT NULL COMMENT '平均置信度',
  `status`               TINYINT NOT NULL DEFAULT 0 COMMENT '任务状态:0待执行 1执行中 2已完成 3已失败 4已取消',
  `fail_reason`          TEXT COMMENT '失败原因',
  `started_at`           DATETIME DEFAULT NULL COMMENT '开始时间',
  `finished_at`          DATETIME DEFAULT NULL COMMENT '完成时间',
  `remark`               VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `created_by`           BIGINT DEFAULT NULL COMMENT '创建人',
  `created_at`           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_no` (`task_no`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='识别任务表';

-- ----------------------------
-- 任务图像关联表
-- ----------------------------
DROP TABLE IF EXISTS `task_image_rel`;
CREATE TABLE `task_image_rel` (
  `task_id`       BIGINT NOT NULL COMMENT '任务ID',
  `image_id`      BIGINT NOT NULL COMMENT '图像ID',
  `result_status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态:0待处理 1成功 2失败',
  PRIMARY KEY (`task_id`, `image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务图像关联表';

-- ----------------------------
-- 识别结果表
-- ----------------------------
DROP TABLE IF EXISTS `recognition_result`;
CREATE TABLE `recognition_result` (
  `id`            BIGINT NOT NULL AUTO_INCREMENT COMMENT '结果ID',
  `task_id`       BIGINT NOT NULL COMMENT '任务ID',
  `image_id`      BIGINT NOT NULL COMMENT '图像ID',
  `review_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核 1已确认 2需修正 3已修正',
  `reviewed_by`   BIGINT DEFAULT NULL COMMENT '审核人',
  `reviewed_at`   DATETIME DEFAULT NULL COMMENT '审核时间',
  `created_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_image_id` (`image_id`),
  KEY `idx_review_status` (`review_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='识别结果表';

-- ----------------------------
-- 检测框表
-- ----------------------------
DROP TABLE IF EXISTS `recognition_box`;
CREATE TABLE `recognition_box` (
  `id`         BIGINT NOT NULL AUTO_INCREMENT COMMENT '检测框ID',
  `result_id`  BIGINT NOT NULL COMMENT '识别结果ID',
  `x`          DECIMAL(10,4) NOT NULL COMMENT '左上角X坐标(相对)',
  `y`          DECIMAL(10,4) NOT NULL COMMENT '左上角Y坐标(相对)',
  `width`      DECIMAL(10,4) NOT NULL COMMENT '宽度(相对)',
  `height`     DECIMAL(10,4) NOT NULL COMMENT '高度(相对)',
  `label`      VARCHAR(64) NOT NULL COMMENT '缺陷标签',
  `confidence` DECIMAL(5,4) DEFAULT NULL COMMENT '置信度',
  `source`     TINYINT NOT NULL DEFAULT 0 COMMENT '来源:0算法 1人工',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
  `updated_by` BIGINT DEFAULT NULL COMMENT '修改人',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_result_id` (`result_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测框表';

-- ----------------------------
-- 报告任务汇总表
-- ----------------------------
DROP TABLE IF EXISTS `report_task_summary`;
CREATE TABLE `report_task_summary` (
  `id`                      BIGINT NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `task_id`                 BIGINT NOT NULL COMMENT '任务ID',
  `total_images`            INT NOT NULL DEFAULT 0 COMMENT '总图像数',
  `success_count`           INT NOT NULL DEFAULT 0 COMMENT '识别成功数',
  `fail_count`              INT NOT NULL DEFAULT 0 COMMENT '识别失败数',
  `avg_confidence`          DECIMAL(5,4) DEFAULT NULL COMMENT '平均置信度',
  `min_confidence`          DECIMAL(5,4) DEFAULT NULL COMMENT '最低置信度',
  `max_confidence`          DECIMAL(5,4) DEFAULT NULL COMMENT '最高置信度',
  `low_confidence_count`    INT NOT NULL DEFAULT 0 COMMENT '低置信度数量(<0.6)',
  `category_stats`          JSON DEFAULT NULL COMMENT '各类别统计(JSON)',
  `confidence_distribution` JSON DEFAULT NULL COMMENT '置信度分布(JSON)',
  `generated_at`            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告任务汇总表';


-- ============================================================
-- 初始化数据
-- ============================================================

-- 初始角色: 超级管理员/算法工程师/图像分析员/只读用户
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`, `status`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统最高权限角色', 1),
('算法工程师', 'ALGO_ENG',    '负责模型版本管理和任务管理', 1),
('图像分析员', 'IMG_ANALYST', '负责图像上传和结果审核', 1),
('只读用户',   'READONLY',    '仅查看权限', 1);

-- 初始用户: admin, 密码 Admin@123 (BCrypt)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `department`, `status`) VALUES
('admin', '$2a$10$7JB720yubVSOfXsN9Qc9Oep7EDfCGZkxcV0JcAg0w.RqD5G9Mvzuy', '系统管理员', '技术部', 1);

-- 给 admin 分配超级管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 初始化图像分类
INSERT INTO `image_category` (`category_name`, `parent_id`, `sort_order`, `created_by`) VALUES
('工业零件', 0, 1, 1),
('表面检测', 0, 2, 1),
('医疗影像', 0, 3, 1),
('焊缝检测', 1, 1, 1),
('裂纹检测', 1, 2, 1),
('腐蚀检测', 2, 1, 1);

-- 初始化模型版本
INSERT INTO `model_version` (`model_name`, `version_no`, `scene_desc`, `support_labels`, `release_date`, `status`, `created_by`) VALUES
('YOLOv8-Defect', 'v1.0.0', '通用工业缺陷检测模型', '裂纹,腐蚀,气泡,划痕,脏污', '2024-01-01', 1, 1),
('ResNet50-Classify', 'v2.1.0', '图像分类模型，适用于医疗影像辅助诊断', '正常,异常,待复查', '2024-03-15', 1, 1);

