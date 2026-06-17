-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: angu_resume_job_matcher_dev
-- ------------------------------------------------------
-- Server version	8.4.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `application_log`
--

DROP TABLE IF EXISTS `application_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_id` bigint NOT NULL,
  `from_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `operator_id` bigint DEFAULT NULL,
  `operator_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_application_log_application_id` (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_log`
--

LOCK TABLES `application_log` WRITE;
/*!40000 ALTER TABLE `application_log` DISABLE KEYS */;
INSERT INTO `application_log` VALUES (1,1,'PENDING','RESUME_PASSED',1,'System Admin','接口测试简历通过','2026-05-27 16:29:44'),(2,1,'RESUME_PASSED','INTERVIEW_WAITING',1,'System Admin','接口测试待面试','2026-05-27 16:29:44'),(3,1,'INTERVIEW_WAITING','INTERVIEWING',1,'System Admin','接口测试面试中','2026-05-27 16:29:44'),(4,1,'INTERVIEWING','INTERVIEW_PASSED',1,'System Admin','面试结果：PASS','2026-05-27 16:29:53'),(5,2,'PENDING','RESUME_PASSED',1,'System Admin','回归简历通过','2026-05-27 16:36:50'),(6,2,'RESUME_PASSED','INTERVIEW_WAITING',1,'System Admin','回归待面试','2026-05-27 16:36:50'),(7,2,'INTERVIEW_WAITING','INTERVIEWING',1,'System Admin','创建面试后进入面试中','2026-05-27 16:36:50'),(8,2,'INTERVIEWING','INTERVIEW_PASSED',1,'System Admin','面试结果：PASS','2026-05-27 16:37:31');
/*!40000 ALTER TABLE `application_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interview_record`
--

DROP TABLE IF EXISTS `interview_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_id` bigint NOT NULL,
  `interview_time` datetime DEFAULT NULL,
  `interviewer` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `score` tinyint DEFAULT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_interview_record_application_id` (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview_record`
--

LOCK TABLES `interview_record` WRITE;
/*!40000 ALTER TABLE `interview_record` DISABLE KEYS */;
INSERT INTO `interview_record` VALUES (1,1,'2026-05-27 19:30:00','面试官A','线上会议室-自动化',5,'接口测试面试通过','PASS','2026-05-27 16:29:44'),(2,2,'2026-05-27 20:00:00','面试官B','线上会议室-回归',5,'回归面试通过','PASS','2026-05-27 16:36:50');
/*!40000 ALTER TABLE `interview_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_application`
--

DROP TABLE IF EXISTS `job_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_id` bigint NOT NULL,
  `resume_id` bigint NOT NULL,
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `operator_id` bigint DEFAULT NULL,
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_position_resume` (`position_id`,`resume_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_application`
--

LOCK TABLES `job_application` WRITE;
/*!40000 ALTER TABLE `job_application` DISABLE KEYS */;
INSERT INTO `job_application` VALUES (1,1,1,'INTERVIEW_PASSED','2026-05-27 16:29:53',1,'面试结果：PASS','2026-05-27 16:19:47'),(2,1,2,'INTERVIEW_PASSED','2026-05-27 16:37:31',1,'面试结果：PASS','2026-05-27 16:36:50');
/*!40000 ALTER TABLE `job_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_position`
--

DROP TABLE IF EXISTS `job_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_position` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `department` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `job_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `salary_min` int DEFAULT NULL,
  `salary_max` int DEFAULT NULL,
  `edu_require` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `exp_require` int DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `skill_tags` json DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `creator_id` bigint DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_position`
--

LOCK TABLES `job_position` WRITE;
/*!40000 ALTER TABLE `job_position` DISABLE KEYS */;
INSERT INTO `job_position` VALUES (1,'自动化测试职位-0527','智能招聘部','FULL_TIME','杭州',10000,20000,'BACHELOR',1,'自动化测试创建的职位，用于验证前端主流程。','[\"Vue3\", \"TypeScript\", \"招聘系统\"]','OPEN',0,1,'2026-05-27 16:17:13','2026-05-27 16:18:52');
/*!40000 ALTER TABLE `job_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_config`
--

DROP TABLE IF EXISTS `match_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_config` (
  `id` bigint NOT NULL,
  `skill_weight` int NOT NULL,
  `edu_weight` int NOT NULL,
  `exp_weight` int NOT NULL,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_config`
--

LOCK TABLES `match_config` WRITE;
/*!40000 ALTER TABLE `match_config` DISABLE KEYS */;
INSERT INTO `match_config` VALUES (1,50,30,20,'2026-05-27 16:59:08',1);
/*!40000 ALTER TABLE `match_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_result`
--

DROP TABLE IF EXISTS `match_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_result` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_id` bigint NOT NULL,
  `resume_id` bigint NOT NULL,
  `total_score` decimal(5,2) NOT NULL,
  `skill_score` decimal(5,2) NOT NULL,
  `edu_score` decimal(5,2) NOT NULL,
  `exp_score` decimal(5,2) NOT NULL,
  `matched_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_match_result_position_id` (`position_id`),
  KEY `idx_match_result_resume_id` (`resume_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_result`
--

LOCK TABLES `match_result` WRITE;
/*!40000 ALTER TABLE `match_result` DISABLE KEYS */;
INSERT INTO `match_result` VALUES (2,1,1,63.34,66.67,100.00,0.00,'2026-05-27 16:59:03'),(3,1,2,63.34,66.67,100.00,0.00,'2026-05-27 16:59:03');
/*!40000 ALTER TABLE `match_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_education`
--

DROP TABLE IF EXISTS `resume_education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_education` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NOT NULL,
  `school` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `major` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `edu_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_resume_education_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_education`
--

LOCK TABLES `resume_education` WRITE;
/*!40000 ALTER TABLE `resume_education` DISABLE KEYS */;
/*!40000 ALTER TABLE `resume_education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_main`
--

DROP TABLE IF EXISTS `resume_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_main` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` tinyint DEFAULT '0',
  `birth_date` date DEFAULT NULL,
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `desired_position` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `desired_city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `desired_salary_min` int DEFAULT NULL,
  `desired_salary_max` int DEFAULT NULL,
  `job_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `highest_edu` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `total_exp_years` int DEFAULT NULL,
  `file_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parse_success` tinyint DEFAULT '0',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'MANUAL',
  `self_intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `creator_id` bigint DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_main`
--

LOCK TABLES `resume_main` WRITE;
/*!40000 ALTER TABLE `resume_main` DISABLE KEYS */;
INSERT INTO `resume_main` VALUES (1,'自动化测试候选人0527','13900005270','auto0527@example.com',NULL,NULL,'杭州','前端开发工程师','杭州',NULL,NULL,'',NULL,NULL,NULL,NULL,'MANUAL','自动化测试创建的简历，用于验证匹配、投递和面试流程。',0,1,'2026-05-27 16:18:40','2026-05-27 16:18:40'),(2,'自动化回归候选人0527B','13900005271','auto0527b@example.com',NULL,NULL,'杭州','前端开发工程师','杭州',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MANUAL','用于验证面试创建自动推进状态',0,1,'2026-05-27 16:36:50','2026-05-27 16:36:50');
/*!40000 ALTER TABLE `resume_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_skill`
--

DROP TABLE IF EXISTS `resume_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_skill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NOT NULL,
  `skill_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_resume_skill_resume_id` (`resume_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_skill`
--

LOCK TABLES `resume_skill` WRITE;
/*!40000 ALTER TABLE `resume_skill` DISABLE KEYS */;
INSERT INTO `resume_skill` VALUES (1,1,'Vue3'),(2,1,'TypeScript'),(3,1,'招聘流程'),(4,2,'Vue3'),(5,2,'TypeScript');
/*!40000 ALTER TABLE `resume_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_work_exp`
--

DROP TABLE IF EXISTS `resume_work_exp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_work_exp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NOT NULL,
  `company` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `position` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `industry` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `idx_resume_work_exp_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_work_exp`
--

LOCK TABLES `resume_work_exp` WRITE;
/*!40000 ALTER TABLE `resume_work_exp` DISABLE KEYS */;
/*!40000 ALTER TABLE `resume_work_exp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL,
  `parent_id` bigint NOT NULL DEFAULT '0',
  `menu_type` tinyint NOT NULL,
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `perm_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,0,'System','/system',NULL,'setting',1),(2,0,0,'Jobs','/jobs',NULL,'briefcase',2),(3,0,0,'Resumes','/resumes',NULL,'file',3),(4,0,0,'Match','/match',NULL,'search',4),(5,0,0,'Recruitment Flow','/flow',NULL,'flow',5),(6,0,0,'Reports','/stats',NULL,'bar-chart',6),(11,1,1,'Users','/system/users',NULL,'user',1),(12,1,1,'Roles','/system/roles',NULL,'team',2),(13,1,1,'Menus','/system/menus',NULL,'menu',3),(21,2,1,'Job Management','/jobs/list',NULL,'briefcase',1),(31,3,1,'Resume Management','/resumes/list',NULL,'file',1),(41,4,1,'Match Management','/match/list',NULL,'search',1),(51,5,1,'Applications','/applications',NULL,'ordered-list',1),(52,5,1,'Interviews','/interviews',NULL,'calendar',2),(61,6,1,'Dashboard','/stats/dashboard',NULL,'dashboard',1),(62,6,1,'Source Report','/stats/source',NULL,'pie-chart',2),(1101,11,2,'User List',NULL,'system:user:list',NULL,1),(1102,11,2,'User Add',NULL,'system:user:add',NULL,2),(1103,11,2,'User Edit',NULL,'system:user:edit',NULL,3),(1104,11,2,'User Delete',NULL,'system:user:delete',NULL,4),(1201,12,2,'Role List',NULL,'system:role:list',NULL,1),(1202,12,2,'Role Add',NULL,'system:role:add',NULL,2),(1203,12,2,'Role Edit',NULL,'system:role:edit',NULL,3),(1204,12,2,'Role Delete',NULL,'system:role:delete',NULL,4),(1301,13,2,'Menu List',NULL,'system:menu:list',NULL,1),(1302,13,2,'Menu Add',NULL,'system:menu:add',NULL,2),(1303,13,2,'Menu Edit',NULL,'system:menu:edit',NULL,3),(1304,13,2,'Menu Delete',NULL,'system:menu:delete',NULL,4),(2101,21,2,'Job List',NULL,'job:job:list',NULL,1),(2102,21,2,'Job Add',NULL,'job:job:add',NULL,2),(2103,21,2,'Job Edit',NULL,'job:job:edit',NULL,3),(2104,21,2,'Job Delete',NULL,'job:job:delete',NULL,4),(2105,21,2,'Job Publish',NULL,'job:job:publish',NULL,5),(2106,21,2,'Job Close',NULL,'job:job:close',NULL,6),(3101,31,2,'Resume List',NULL,'resume:resume:list',NULL,1),(3102,31,2,'Resume Add',NULL,'resume:resume:add',NULL,2),(3103,31,2,'Resume Edit',NULL,'resume:resume:edit',NULL,3),(3104,31,2,'Resume Delete',NULL,'resume:resume:delete',NULL,4),(3105,31,2,'Resume Export',NULL,'resume:resume:export',NULL,5),(4101,41,2,'Match Execute',NULL,'match:match:execute',NULL,1),(4102,41,2,'Match Config View',NULL,'match:config:view',NULL,2),(4103,41,2,'Match Config Edit',NULL,'match:config:edit',NULL,3),(5101,51,2,'Application List',NULL,'application:list',NULL,1),(5102,51,2,'Application Add',NULL,'application:add',NULL,2),(5103,51,2,'Application Edit',NULL,'application:edit',NULL,3),(5201,52,2,'Interview List',NULL,'interview:list',NULL,1),(5202,52,2,'Interview Add',NULL,'interview:add',NULL,2),(5203,52,2,'Interview Edit',NULL,'interview:edit',NULL,3),(6101,61,2,'Dashboard View',NULL,'stats:dashboard:view',NULL,1),(6201,62,2,'Source Report View',NULL,'stats:report:view',NULL,1),(6202,62,2,'Source Report Export',NULL,'stats:report:export',NULL,2);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `builtin` tinyint NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '1',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'Super Admin','SUPER_ADMIN',1,1,'Built-in super admin','2026-05-27 14:37:46','2026-05-27 14:37:46'),(2,'HR Admin','HR_ADMIN',0,1,'HR admin','2026-05-27 14:37:46','2026-05-27 14:37:46'),(3,'HR Staff','HR_STAFF',0,1,'HR staff','2026-05-27 14:37:46','2026-05-27 14:37:46');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,11),(1,12),(1,13),(1,21),(1,31),(1,41),(1,51),(1,52),(1,61),(1,62),(1,1101),(1,1102),(1,1103),(1,1104),(1,1201),(1,1202),(1,1203),(1,1204),(1,1301),(1,1302),(1,1303),(1,1304),(1,2101),(1,2102),(1,2103),(1,2104),(1,2105),(1,2106),(1,3101),(1,3102),(1,3103),(1,3104),(1,3105),(1,4101),(1,4102),(1,4103),(1,5101),(1,5102),(1,5103),(1,5201),(1,5202),(1,5203),(1,6101),(1,6201),(1,6202);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `error_count` int NOT NULL DEFAULT '0',
  `locked_until` datetime DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','System Admin','13800000000','$2a$10$CHHVPySZmO5DsDtGjAXwXugfDw4hPiI4HiOGoc8MSuyJqVM1e000q',1,0,NULL,0,'2026-05-27 14:37:46','2026-05-27 14:37:46');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'angu_resume_job_matcher_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:07:23
