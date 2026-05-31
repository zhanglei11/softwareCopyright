CREATE DATABASE IF NOT EXISTS angu_resume_job_matcher_dev
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE angu_resume_job_matcher_dev;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    real_name VARCHAR(64) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    error_count INT NOT NULL DEFAULT 0,
    locked_until DATETIME NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(64) NOT NULL,
    role_code VARCHAR(64) NOT NULL UNIQUE,
    builtin TINYINT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY,
    parent_id BIGINT NOT NULL DEFAULT 0,
    menu_type TINYINT NOT NULL,
    menu_name VARCHAR(64) NOT NULL,
    path VARCHAR(255),
    perm_code VARCHAR(128),
    icon VARCHAR(64),
    sort INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

CREATE TABLE IF NOT EXISTS job_position (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(128) NOT NULL,
    department VARCHAR(64),
    job_type VARCHAR(20),
    location VARCHAR(128),
    salary_min INT,
    salary_max INT,
    edu_require VARCHAR(20),
    exp_require INT,
    description TEXT,
    skill_tags JSON,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    deleted TINYINT NOT NULL DEFAULT 0,
    creator_id BIGINT,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS resume_main (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64),
    phone VARCHAR(20),
    email VARCHAR(128),
    gender TINYINT DEFAULT 0,
    birth_date DATE,
    city VARCHAR(64),
    desired_position VARCHAR(128),
    desired_city VARCHAR(64),
    desired_salary_min INT,
    desired_salary_max INT,
    job_status VARCHAR(20),
    highest_edu VARCHAR(20),
    total_exp_years INT,
    file_path VARCHAR(512),
    parse_success TINYINT DEFAULT 0,
    source VARCHAR(20) DEFAULT 'MANUAL',
    self_intro TEXT,
    deleted TINYINT NOT NULL DEFAULT 0,
    creator_id BIGINT,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS resume_education (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    school VARCHAR(128),
    major VARCHAR(128),
    edu_level VARCHAR(20),
    start_date DATE,
    end_date DATE,
    KEY idx_resume_education_resume_id (resume_id)
);

CREATE TABLE IF NOT EXISTS resume_work_exp (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    company VARCHAR(128),
    position VARCHAR(128),
    industry VARCHAR(128),
    start_date DATE,
    end_date DATE,
    description TEXT,
    KEY idx_resume_work_exp_resume_id (resume_id)
);

CREATE TABLE IF NOT EXISTS resume_skill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    skill_name VARCHAR(128) NOT NULL,
    KEY idx_resume_skill_resume_id (resume_id)
);

CREATE TABLE IF NOT EXISTS job_application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_id BIGINT NOT NULL,
    resume_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    operate_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operator_id BIGINT,
    remark VARCHAR(512),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_position_resume (position_id, resume_id)
);

CREATE TABLE IF NOT EXISTS application_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    application_id BIGINT NOT NULL,
    from_status VARCHAR(30),
    to_status VARCHAR(30) NOT NULL,
    operator_id BIGINT,
    operator_name VARCHAR(64),
    remark VARCHAR(512),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_application_log_application_id (application_id)
);

CREATE TABLE IF NOT EXISTS interview_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    application_id BIGINT NOT NULL,
    interview_time DATETIME,
    interviewer VARCHAR(64),
    location VARCHAR(255),
    score TINYINT,
    comment TEXT,
    result VARCHAR(20),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_interview_record_application_id (application_id)
);

CREATE TABLE IF NOT EXISTS match_result (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_id BIGINT NOT NULL,
    resume_id BIGINT NOT NULL,
    total_score DECIMAL(5,2) NOT NULL,
    skill_score DECIMAL(5,2) NOT NULL,
    edu_score DECIMAL(5,2) NOT NULL,
    exp_score DECIMAL(5,2) NOT NULL,
    matched_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_match_result_position_id (position_id),
    KEY idx_match_result_resume_id (resume_id)
);

CREATE TABLE IF NOT EXISTS match_config (
    id BIGINT PRIMARY KEY,
    skill_weight INT NOT NULL,
    edu_weight INT NOT NULL,
    exp_weight INT NOT NULL,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater_id BIGINT
);

INSERT INTO sys_role (id, role_name, role_code, builtin, status, remark)
VALUES
    (1, 'Super Admin', 'SUPER_ADMIN', 1, 1, 'Built-in super admin'),
    (2, 'HR Admin', 'HR_ADMIN', 0, 1, 'HR admin'),
    (3, 'HR Staff', 'HR_STAFF', 0, 1, 'HR staff')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name), status = VALUES(status), remark = VALUES(remark);

INSERT INTO sys_user (id, username, real_name, phone, password, status, error_count, deleted)
VALUES
    (1, 'admin', 'System Admin', '13800000000', '$2a$10$CHHVPySZmO5DsDtGjAXwXugfDw4hPiI4HiOGoc8MSuyJqVM1e000q', 1, 0, 0)
ON DUPLICATE KEY UPDATE real_name = VALUES(real_name), phone = VALUES(phone), status = VALUES(status), deleted = VALUES(deleted);

INSERT INTO sys_user_role (user_id, role_id)
VALUES (1, 1)
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

INSERT INTO sys_menu (id, parent_id, menu_type, menu_name, path, perm_code, icon, sort)
VALUES
    (1, 0, 0, 'System', '/system', NULL, 'setting', 1),
    (11, 1, 1, 'Users', '/system/users', NULL, 'user', 1),
    (12, 1, 1, 'Roles', '/system/roles', NULL, 'team', 2),
    (13, 1, 1, 'Menus', '/system/menus', NULL, 'menu', 3),
    (1101, 11, 2, 'User List', NULL, 'system:user:list', NULL, 1),
    (1102, 11, 2, 'User Add', NULL, 'system:user:add', NULL, 2),
    (1103, 11, 2, 'User Edit', NULL, 'system:user:edit', NULL, 3),
    (1104, 11, 2, 'User Delete', NULL, 'system:user:delete', NULL, 4),
    (1201, 12, 2, 'Role List', NULL, 'system:role:list', NULL, 1),
    (1202, 12, 2, 'Role Add', NULL, 'system:role:add', NULL, 2),
    (1203, 12, 2, 'Role Edit', NULL, 'system:role:edit', NULL, 3),
    (1204, 12, 2, 'Role Delete', NULL, 'system:role:delete', NULL, 4),
    (1301, 13, 2, 'Menu List', NULL, 'system:menu:list', NULL, 1),
    (1302, 13, 2, 'Menu Add', NULL, 'system:menu:add', NULL, 2),
    (1303, 13, 2, 'Menu Edit', NULL, 'system:menu:edit', NULL, 3),
    (1304, 13, 2, 'Menu Delete', NULL, 'system:menu:delete', NULL, 4),
    (2, 0, 0, 'Jobs', '/jobs', NULL, 'briefcase', 2),
    (21, 2, 1, 'Job Management', '/jobs/list', NULL, 'briefcase', 1),
    (2101, 21, 2, 'Job List', NULL, 'job:job:list', NULL, 1),
    (2102, 21, 2, 'Job Add', NULL, 'job:job:add', NULL, 2),
    (2103, 21, 2, 'Job Edit', NULL, 'job:job:edit', NULL, 3),
    (2104, 21, 2, 'Job Delete', NULL, 'job:job:delete', NULL, 4),
    (2105, 21, 2, 'Job Publish', NULL, 'job:job:publish', NULL, 5),
    (2106, 21, 2, 'Job Close', NULL, 'job:job:close', NULL, 6),
    (3, 0, 0, 'Resumes', '/resumes', NULL, 'file', 3),
    (31, 3, 1, 'Resume Management', '/resumes/list', NULL, 'file', 1),
    (3101, 31, 2, 'Resume List', NULL, 'resume:resume:list', NULL, 1),
    (3102, 31, 2, 'Resume Add', NULL, 'resume:resume:add', NULL, 2),
    (3103, 31, 2, 'Resume Edit', NULL, 'resume:resume:edit', NULL, 3),
    (3104, 31, 2, 'Resume Delete', NULL, 'resume:resume:delete', NULL, 4),
    (3105, 31, 2, 'Resume Export', NULL, 'resume:resume:export', NULL, 5),
    (4, 0, 0, 'Match', '/match', NULL, 'search', 4),
    (41, 4, 1, 'Match Management', '/match/list', NULL, 'search', 1),
    (4101, 41, 2, 'Match Execute', NULL, 'match:match:execute', NULL, 1),
    (4102, 41, 2, 'Match Config View', NULL, 'match:config:view', NULL, 2),
    (4103, 41, 2, 'Match Config Edit', NULL, 'match:config:edit', NULL, 3),
    (5, 0, 0, 'Recruitment Flow', '/flow', NULL, 'flow', 5),
    (51, 5, 1, 'Applications', '/applications', NULL, 'ordered-list', 1),
    (52, 5, 1, 'Interviews', '/interviews', NULL, 'calendar', 2),
    (5101, 51, 2, 'Application List', NULL, 'application:list', NULL, 1),
    (5102, 51, 2, 'Application Add', NULL, 'application:add', NULL, 2),
    (5103, 51, 2, 'Application Edit', NULL, 'application:edit', NULL, 3),
    (5201, 52, 2, 'Interview List', NULL, 'interview:list', NULL, 1),
    (5202, 52, 2, 'Interview Add', NULL, 'interview:add', NULL, 2),
    (5203, 52, 2, 'Interview Edit', NULL, 'interview:edit', NULL, 3),
    (6, 0, 0, 'Reports', '/stats', NULL, 'bar-chart', 6),
    (61, 6, 1, 'Dashboard', '/stats/dashboard', NULL, 'dashboard', 1),
    (62, 6, 1, 'Source Report', '/stats/source', NULL, 'pie-chart', 2),
    (6101, 61, 2, 'Dashboard View', NULL, 'stats:dashboard:view', NULL, 1),
    (6201, 62, 2, 'Source Report View', NULL, 'stats:report:view', NULL, 1),
    (6202, 62, 2, 'Source Report Export', NULL, 'stats:report:export', NULL, 2)
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), path = VALUES(path), perm_code = VALUES(perm_code), sort = VALUES(sort);

INSERT INTO sys_role_menu (role_id, menu_id)
VALUES
    (1, 1), (1, 11), (1, 12), (1, 13),
    (1, 1101), (1, 1102), (1, 1103), (1, 1104),
    (1, 1201), (1, 1202), (1, 1203), (1, 1204),
    (1, 1301), (1, 1302), (1, 1303), (1, 1304),
    (1, 2), (1, 21),
    (1, 2101), (1, 2102), (1, 2103), (1, 2104), (1, 2105), (1, 2106),
    (1, 3), (1, 31),
    (1, 3101), (1, 3102), (1, 3103), (1, 3104), (1, 3105),
    (1, 4), (1, 41),
    (1, 4101), (1, 4102), (1, 4103),
    (1, 5), (1, 51), (1, 52),
    (1, 5101), (1, 5102), (1, 5103),
    (1, 5201), (1, 5202), (1, 5203),
    (1, 6), (1, 61), (1, 62),
    (1, 6101), (1, 6201), (1, 6202)
ON DUPLICATE KEY UPDATE menu_id = VALUES(menu_id);

INSERT INTO match_config (id, skill_weight, edu_weight, exp_weight, updated_time, updater_id)
VALUES (1, 50, 30, 20, NOW(), 1)
ON DUPLICATE KEY UPDATE skill_weight = VALUES(skill_weight), edu_weight = VALUES(edu_weight), exp_weight = VALUES(exp_weight), updated_time = NOW(), updater_id = VALUES(updater_id);