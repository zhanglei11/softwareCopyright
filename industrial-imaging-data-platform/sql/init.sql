-- ============================================================
-- 多场景影像传感设备资源协同调度系统 · 数据库初始化脚本
-- 数据库：industrial_imaging_data_platform_dev
-- 版本：V1.0  日期：2026-05-27
-- ============================================================
CREATE DATABASE IF NOT EXISTS industrial_imaging_data_platform_dev
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
USE industrial_imaging_data_platform_dev;

-- ----------------------------
-- 1. 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username     VARCHAR(50)  NOT NULL                COMMENT '用户名',
    password     VARCHAR(100) NOT NULL                COMMENT '密码(BCrypt)',
    real_name    VARCHAR(50)  DEFAULT NULL            COMMENT '真实姓名',
    email        VARCHAR(100) DEFAULT NULL            COMMENT '邮箱',
    phone        VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
    avatar       VARCHAR(255) DEFAULT NULL            COMMENT '头像URL',
    status       TINYINT      NOT NULL DEFAULT 1      COMMENT '状态:0停用1启用',
    is_deleted   TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除:0否1是',
    remark       VARCHAR(500) DEFAULT NULL            COMMENT '备注',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by   BIGINT       DEFAULT NULL            COMMENT '创建人',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by   BIGINT       DEFAULT NULL            COMMENT '更新人',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 初始管理员账号 密码: Admin@123
INSERT INTO sys_user(id, username, password, real_name, status)
VALUES (1, 'admin', '$2a$10$nRttHrXGoxfCcNYhWLeZu.FwLfiuLVzAB8KyOdJorWnChYdZNztQW', '超级管理员', 1);

-- ----------------------------
-- 2. 角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name   VARCHAR(50)  NOT NULL                COMMENT '角色名称',
    role_key    VARCHAR(100) NOT NULL                COMMENT '角色标识',
    sort_order  INT          NOT NULL DEFAULT 0       COMMENT '排序',
    status      TINYINT      NOT NULL DEFAULT 1       COMMENT '状态:0停用1启用',
    is_deleted  TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除',
    remark      VARCHAR(500) DEFAULT NULL             COMMENT '备注',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  BIGINT       DEFAULT NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by  BIGINT       DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_key (role_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

INSERT INTO sys_role(id, role_name, role_key, sort_order) VALUES
(1, '超级管理员', 'admin', 1),
(2, '数据工程师', 'data_engineer', 2),
(3, '业务分析师', 'analyst', 3),
(4, '管理人员', 'manager', 4);

-- ----------------------------
-- 3. 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    parent_id   BIGINT       NOT NULL DEFAULT 0       COMMENT '父菜单ID',
    menu_name   VARCHAR(50)  NOT NULL                 COMMENT '菜单名称',
    menu_type   CHAR(1)      NOT NULL DEFAULT 'M'     COMMENT '类型:M目录C菜单F按钮',
    path        VARCHAR(200) DEFAULT NULL             COMMENT '路由路径',
    component   VARCHAR(255) DEFAULT NULL             COMMENT '组件路径',
    perms       VARCHAR(100) DEFAULT NULL             COMMENT '权限标识',
    icon        VARCHAR(100) DEFAULT '#'              COMMENT '菜单图标',
    sort_order  INT          NOT NULL DEFAULT 0        COMMENT '排序',
    visible     TINYINT      NOT NULL DEFAULT 1        COMMENT '是否显示:0否1是',
    status      TINYINT      NOT NULL DEFAULT 1        COMMENT '状态:0停用1启用',
    is_deleted  TINYINT      NOT NULL DEFAULT 0        COMMENT '逻辑删除',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  BIGINT       DEFAULT NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by  BIGINT       DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

INSERT INTO sys_menu(id, parent_id, menu_name, menu_type, path, perms, sort_order) VALUES
(1,  0, '基础管理',     'M', '/system',    NULL,                      1),
(2,  1, '用户管理',     'C', 'user',       'system:user:list',        1),
(3,  1, '角色管理',     'C', 'role',       'system:role:list',        2),
(4,  1, '菜单管理',     'C', 'menu',       'system:menu:list',        3),
(5,  1, '操作日志',     'C', 'oper-log',   'logs:operation:list',     4),
(10, 0, '数据源管理',   'C', '/datasource','datasource:config:list',  2),
(20, 0, '数据接入管理', 'M', '/ingest',    NULL,                      3),
(21,20, '接入任务',     'C', 'task',       'ingest:task:list',        1),
(22,20, '接入记录',     'C', 'record',     'ingest:record:list',      2),
(30, 0, '数据处理管理', 'M', '/process',   NULL,                      4),
(31,30, '处理任务',     'C', 'task',       'process:task:list',       1),
(32,30, '执行监控',     'C', 'monitor',    'process:execution:view',  2),
(33,30, '处理结果',     'C', 'result',     'process:result:list',     3),
(40, 0, '数据存储管理', 'M', '/storage',   NULL,                      5),
(41,40, '存储总览',     'C', 'overview',   'storage:overview:view',   1),
(42,40, '清理规则',     'C', 'clean-rule', 'storage:clean:list',      2),
(43,40, '清理日志',     'C', 'clean-log',  'storage:clean:log',       3),
(50, 0, '统计分析',     'M', '/stats',     NULL,                      6),
(51,50, '数据总览',     'C', 'overview',   'stats:overview:view',     1),
(52,50, '处理统计',     'C', 'process',    'stats:process:view',      2),
(53,50, '趋势分析',     'C', 'trend',      'stats:analysis:view',     3);

-- ----------------------------
-- 4. 用户角色关联
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

INSERT INTO sys_user_role(user_id, role_id) VALUES (1, 1);

-- ----------------------------
-- 5. 角色菜单关联
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 6. 操作日志
-- ----------------------------
DROP TABLE IF EXISTS sys_oper_log;
CREATE TABLE sys_oper_log (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    title          VARCHAR(50)  DEFAULT ''    COMMENT '模块标题',
    business_type  TINYINT      DEFAULT 0     COMMENT '业务类型:0其他1新增2修改3删除4查询',
    method         VARCHAR(200) DEFAULT ''    COMMENT '方法名称',
    request_method VARCHAR(10)  DEFAULT ''    COMMENT '请求方式',
    operator_type  TINYINT      DEFAULT 0     COMMENT '操作类别:0其他1后台用户',
    oper_name      VARCHAR(50)  DEFAULT ''    COMMENT '操作人员',
    oper_url       VARCHAR(255) DEFAULT ''    COMMENT '请求URL',
    oper_ip        VARCHAR(50)  DEFAULT ''    COMMENT '操作地址',
    oper_param     TEXT                       COMMENT '请求参数',
    json_result    TEXT                       COMMENT '返回参数',
    status         TINYINT      DEFAULT 0     COMMENT '操作状态:0正常1异常',
    error_msg      TEXT                       COMMENT '错误消息',
    oper_time      DATETIME     DEFAULT NULL  COMMENT '操作时间',
    cost_time      BIGINT       DEFAULT 0     COMMENT '消耗时间(ms)',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- 7. 数据源配置表
-- ----------------------------
DROP TABLE IF EXISTS datasource_config;
CREATE TABLE datasource_config (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '数据源ID',
    datasource_code  VARCHAR(30)  NOT NULL                COMMENT '数据源编号(自动)',
    datasource_name  VARCHAR(100) NOT NULL                COMMENT '数据源名称',
    datasource_type  VARCHAR(20)  NOT NULL                COMMENT '类型:DEVICE/FILE_SERVER/DATABASE/OBJECT_STORAGE',
    host             VARCHAR(255) NOT NULL                COMMENT '连接地址',
    port             INT          NOT NULL                COMMENT '端口',
    auth_type        VARCHAR(20)  NOT NULL DEFAULT 'NONE' COMMENT '认证方式:NONE/PASSWORD/KEY',
    auth_username    VARCHAR(100) DEFAULT NULL            COMMENT '认证账号',
    auth_password    VARCHAR(500) DEFAULT NULL            COMMENT '认证密码(加密存储)',
    auth_key         TEXT         DEFAULT NULL            COMMENT '密钥(加密存储)',
    data_format      VARCHAR(20)  NOT NULL                COMMENT '数据格式:JPEG/PNG/RAW/MP4/OTHER',
    ext_config       JSON         DEFAULT NULL            COMMENT '扩展配置JSON',
    owner_id         BIGINT       DEFAULT NULL            COMMENT '负责人用户ID',
    status           TINYINT      NOT NULL DEFAULT 1      COMMENT '状态:0停用1启用',
    is_deleted       TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    remark           VARCHAR(500) DEFAULT NULL            COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       BIGINT       DEFAULT NULL,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       BIGINT       DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_datasource_code (datasource_code),
    UNIQUE KEY uk_datasource_name (datasource_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源配置表';

-- ----------------------------
-- 8. 数据源连接日志表
-- ----------------------------
DROP TABLE IF EXISTS datasource_conn_log;
CREATE TABLE datasource_conn_log (
    id             BIGINT    NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    datasource_id  BIGINT    NOT NULL                COMMENT '数据源ID',
    result         TINYINT   NOT NULL                COMMENT '检测结果:0失败1成功',
    error_msg      TEXT      DEFAULT NULL            COMMENT '失败原因',
    cost_time      INT       DEFAULT 0               COMMENT '耗时(ms)',
    tested_at      DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
    tested_by      BIGINT    NOT NULL DEFAULT 0      COMMENT '执行人(0=系统)',
    PRIMARY KEY (id),
    KEY idx_datasource_id (datasource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源连接测试日志';

-- ----------------------------
-- 9. 接入任务表
-- ----------------------------
DROP TABLE IF EXISTS ingest_task;
CREATE TABLE ingest_task (
    id                   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    task_code            VARCHAR(30)  NOT NULL                COMMENT '任务编号',
    task_name            VARCHAR(100) NOT NULL                COMMENT '任务名称',
    datasource_id        BIGINT       NOT NULL                COMMENT '数据源ID',
    ingest_type          VARCHAR(20)  NOT NULL                COMMENT '接入方式:REALTIME/SCHEDULED',
    cron_expression      VARCHAR(100) DEFAULT NULL            COMMENT '调度表达式',
    filter_file_types    VARCHAR(200) DEFAULT NULL            COMMENT '文件类型过滤(JSON数组)',
    filter_file_pattern  VARCHAR(255) DEFAULT NULL            COMMENT '文件名规则',
    filter_start_time    DATETIME     DEFAULT NULL            COMMENT '时间过滤起始',
    filter_end_time      DATETIME     DEFAULT NULL            COMMENT '时间过滤结束',
    storage_dir          VARCHAR(500) NOT NULL                COMMENT '存储目录',
    status               TINYINT      NOT NULL DEFAULT 0      COMMENT '状态:0停用1启用',
    is_deleted           TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    created_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by           BIGINT       DEFAULT NULL,
    updated_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by           BIGINT       DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_task_code (task_code),
    UNIQUE KEY uk_task_name (task_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接入任务表';

-- ----------------------------
-- 10. 接入记录表
-- ----------------------------
DROP TABLE IF EXISTS ingest_record;
CREATE TABLE ingest_record (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    record_code     VARCHAR(30)  NOT NULL                COMMENT '记录编号',
    task_id         BIGINT       NOT NULL                COMMENT '接入任务ID',
    task_name       VARCHAR(100) NOT NULL                COMMENT '任务名称快照',
    start_time      DATETIME     NOT NULL                COMMENT '开始时间',
    end_time        DATETIME     DEFAULT NULL            COMMENT '完成时间',
    cost_seconds    INT          DEFAULT 0               COMMENT '耗时(秒)',
    ingest_count    INT          DEFAULT 0               COMMENT '接入文件数',
    data_size_bytes BIGINT       DEFAULT 0               COMMENT '数据大小(字节)',
    execute_status  VARCHAR(20)  NOT NULL DEFAULT 'RUNNING' COMMENT '状态:SUCCESS/PARTIAL/FAILED',
    fail_reason     TEXT         DEFAULT NULL            COMMENT '失败原因',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_record_code (record_code),
    KEY idx_task_id (task_id),
    KEY idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接入记录表';

-- ----------------------------
-- 11. 处理任务表
-- ----------------------------
DROP TABLE IF EXISTS process_task;
CREATE TABLE process_task (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    task_code        VARCHAR(30)  NOT NULL                COMMENT '任务编号',
    task_name        VARCHAR(100) NOT NULL                COMMENT '任务名称',
    input_dir        VARCHAR(500) NOT NULL                COMMENT '输入目录',
    process_type     VARCHAR(30)  NOT NULL                COMMENT '处理类型:IMAGE_COMPRESS/FORMAT_CONVERT/RESOLUTION_RESIZE/BATCH_RENAME/QUALITY_FILTER',
    process_params   JSON         NOT NULL                COMMENT '处理参数JSON',
    output_dir       VARCHAR(500) NOT NULL                COMMENT '输出目录',
    execute_type     VARCHAR(20)  NOT NULL DEFAULT 'MANUAL' COMMENT '执行方式:MANUAL/SCHEDULED',
    cron_expression  VARCHAR(100) DEFAULT NULL            COMMENT '调度表达式',
    status           TINYINT      NOT NULL DEFAULT 0      COMMENT '状态:0停用1启用2执行中3已终止',
    is_deleted       TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       BIGINT       DEFAULT NULL,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       BIGINT       DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_task_code (task_code),
    UNIQUE KEY uk_task_name (task_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='处理任务表';

-- ----------------------------
-- 12. 处理执行记录表
-- ----------------------------
DROP TABLE IF EXISTS process_execution;
CREATE TABLE process_execution (
    id                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '执行ID',
    exec_code           VARCHAR(30)  NOT NULL                COMMENT '执行编号',
    task_id             BIGINT       NOT NULL                COMMENT '处理任务ID',
    task_name           VARCHAR(100) NOT NULL                COMMENT '任务名称快照',
    start_time          DATETIME     NOT NULL                COMMENT '开始时间',
    end_time            DATETIME     DEFAULT NULL            COMMENT '结束时间',
    total_count         INT          DEFAULT 0               COMMENT '处理文件总数',
    success_count       INT          DEFAULT 0               COMMENT '成功数',
    fail_count          INT          DEFAULT 0               COMMENT '失败数',
    output_size_bytes   BIGINT       DEFAULT 0               COMMENT '输出大小(字节)',
    execute_status      VARCHAR(20)  NOT NULL DEFAULT 'RUNNING' COMMENT '状态:RUNNING/COMPLETED/FAILED/TERMINATED',
    fail_file_list      JSON         DEFAULT NULL            COMMENT '失败文件清单',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_exec_code (exec_code),
    KEY idx_task_id (task_id),
    KEY idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='处理执行记录表';

-- ----------------------------
-- 13. 存储快照表
-- ----------------------------
DROP TABLE IF EXISTS storage_snapshot;
CREATE TABLE storage_snapshot (
    id              BIGINT  NOT NULL AUTO_INCREMENT COMMENT '快照ID',
    total_bytes     BIGINT  NOT NULL DEFAULT 0      COMMENT '总容量(字节)',
    used_bytes      BIGINT  NOT NULL DEFAULT 0      COMMENT '已用(字节)',
    free_bytes      BIGINT  NOT NULL DEFAULT 0      COMMENT '可用(字节)',
    usage_rate      DECIMAL(5,2) DEFAULT 0.00       COMMENT '使用率(%)',
    snapshot_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
    PRIMARY KEY (id),
    KEY idx_snapshot_time (snapshot_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储空间快照';

-- ----------------------------
-- 14. 存储目录分布表
-- ----------------------------
DROP TABLE IF EXISTS storage_dir_stat;
CREATE TABLE storage_dir_stat (
    id                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    dir_path            VARCHAR(500) NOT NULL                COMMENT '目录路径',
    size_bytes          BIGINT       NOT NULL DEFAULT 0      COMMENT '占用大小(字节)',
    file_count          INT          NOT NULL DEFAULT 0      COMMENT '文件数量',
    last_modified_at    DATETIME     DEFAULT NULL            COMMENT '最后修改时间',
    snapshot_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
    PRIMARY KEY (id),
    KEY idx_snapshot_time (snapshot_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储目录统计';

-- ----------------------------
-- 15. 数据清理规则表
-- ----------------------------
DROP TABLE IF EXISTS storage_clean_rule;
CREATE TABLE storage_clean_rule (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '规则ID',
    rule_name        VARCHAR(100) NOT NULL                COMMENT '规则名称',
    target_dir       VARCHAR(500) NOT NULL                COMMENT '目标清理目录',
    condition_type   VARCHAR(30)  NOT NULL                COMMENT '清理条件:LAST_ACCESS_DAYS/BEFORE_DATE/FILE_SIZE',
    condition_value  JSON         NOT NULL                COMMENT '条件值JSON',
    execute_type     VARCHAR(20)  NOT NULL DEFAULT 'MANUAL' COMMENT '执行方式:MANUAL/SCHEDULED',
    cron_expression  VARCHAR(100) DEFAULT NULL            COMMENT '调度表达式',
    after_action     VARCHAR(20)  NOT NULL DEFAULT 'DELETE' COMMENT '清理后操作:DELETE/ARCHIVE',
    archive_dir      VARCHAR(500) DEFAULT NULL            COMMENT '归档目录',
    status           TINYINT      NOT NULL DEFAULT 1      COMMENT '状态:0停用1启用',
    is_deleted       TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    remark           VARCHAR(500) DEFAULT NULL,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       BIGINT       DEFAULT NULL,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by       BIGINT       DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_rule_name (rule_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据清理规则表';

-- ----------------------------
-- 16. 数据清理日志表
-- ----------------------------
DROP TABLE IF EXISTS storage_clean_log;
CREATE TABLE storage_clean_log (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    rule_id         BIGINT       NOT NULL                COMMENT '清理规则ID',
    rule_name       VARCHAR(100) NOT NULL                COMMENT '规则名称快照',
    execute_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    execute_type    VARCHAR(20)  NOT NULL                COMMENT '触发方式:MANUAL/SCHEDULED',
    deleted_count   INT          DEFAULT 0               COMMENT '删除/归档文件数',
    freed_bytes     BIGINT       DEFAULT 0               COMMENT '释放空间(字节)',
    execute_status  VARCHAR(20)  NOT NULL DEFAULT 'SUCCESS' COMMENT '执行状态:SUCCESS/FAILED',
    error_msg       TEXT         DEFAULT NULL            COMMENT '错误信息',
    executed_by     BIGINT       DEFAULT 0               COMMENT '执行人(0=系统)',
    PRIMARY KEY (id),
    KEY idx_rule_id (rule_id),
    KEY idx_execute_time (execute_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据清理日志表';
