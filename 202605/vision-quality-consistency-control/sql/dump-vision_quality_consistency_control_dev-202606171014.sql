-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: vision_quality_consistency_control_dev
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
-- Table structure for table `quality_agent`
--

DROP TABLE IF EXISTS `quality_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_agent` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agent_name` varchar(100) NOT NULL COMMENT '智能体名称',
  `agent_code` varchar(50) NOT NULL COMMENT '智能体编码',
  `agent_type` tinyint NOT NULL DEFAULT '1' COMMENT '类型:1=视觉检测,2=尺寸测量,3=缺陷识别',
  `endpoint_url` varchar(255) DEFAULT NULL COMMENT '接入端点URL',
  `auth_token` varchar(500) DEFAULT NULL COMMENT '认证Token',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0=离线,1=空闲,2=运行中',
  `last_heartbeat` datetime DEFAULT NULL COMMENT '最后心跳时间',
  `remark` varchar(500) DEFAULT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `agent_code` (`agent_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI检测智能体';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_agent`
--

LOCK TABLES `quality_agent` WRITE;
/*!40000 ALTER TABLE `quality_agent` DISABLE KEYS */;
INSERT INTO `quality_agent` VALUES (1,'视觉检测Agent-01','AGENT-VD-001',1,'http://agent-host:8080/detect',NULL,2,'2026-05-28 09:37:58','主流水线视觉检测',0,'2026-05-28 09:32:46',1,'2026-05-28 09:37:58',NULL),(2,'缺陷识别Agent-01','AGENT-DF-001',3,'http://agent-host:8081/defect',NULL,1,NULL,'专用缺陷识别模型',0,'2026-05-28 09:32:46',1,'2026-05-28 09:32:46',NULL),(3,'测试尺寸Agent-已更新','AGENT-DIM-TEST',2,'http://test-host:9090/measure-v2','test-token-xyz',1,'2026-05-28 09:37:57','已更新备注',1,'2026-05-28 09:37:57',1,'2026-05-28 09:37:58',1);
/*!40000 ALTER TABLE `quality_agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_agent_task`
--

DROP TABLE IF EXISTS `quality_agent_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_agent_task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agent_id` bigint NOT NULL COMMENT '智能体ID',
  `task_id` bigint NOT NULL COMMENT '检测任务ID',
  `dispatch_status` tinyint NOT NULL DEFAULT '0' COMMENT '调度状态:0=待执行,1=执行中,2=已完成,3=失败',
  `dispatch_at` datetime DEFAULT NULL COMMENT '调度时间',
  `complete_at` datetime DEFAULT NULL COMMENT '完成时间',
  `result_summary` varchar(1000) DEFAULT NULL COMMENT '执行结果摘要',
  `created_by` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='智能体任务分配记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_agent_task`
--

LOCK TABLES `quality_agent_task` WRITE;
/*!40000 ALTER TABLE `quality_agent_task` DISABLE KEYS */;
INSERT INTO `quality_agent_task` VALUES (1,1,3,0,'2026-05-28 09:37:58',NULL,NULL,1,'2026-05-28 09:37:58');
/*!40000 ALTER TABLE `quality_agent_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_defect`
--

DROP TABLE IF EXISTS `quality_defect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_defect` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '不合格品ID',
  `defect_code` varchar(50) NOT NULL COMMENT '记录编号，DEF-{yyyyMMdd}-{序号}',
  `task_id` bigint NOT NULL COMMENT '关联任务ID',
  `image_id` varchar(200) NOT NULL COMMENT '影像标识',
  `exceeded_metrics` json NOT NULL COMMENT '超标指标信息JSON',
  `exceeded_values` json NOT NULL COMMENT '各超标指标实测值JSON',
  `standard_ranges` json NOT NULL COMMENT '各指标标准范围快照JSON',
  `found_at` datetime NOT NULL COMMENT '发现时间',
  `dispose_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1-待处置 2-处置中 3-已处置 4-已忽略',
  `verify_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-待验证 1-已验证合格 2-验证不合格',
  `ignore_reason` varchar(500) DEFAULT NULL COMMENT '忽略原因',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_defect_code` (`defect_code`),
  KEY `idx_task_dispose` (`task_id`,`dispose_status`),
  KEY `idx_found_dispose` (`found_at`,`dispose_status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='不合格品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_defect`
--

LOCK TABLES `quality_defect` WRITE;
/*!40000 ALTER TABLE `quality_defect` DISABLE KEYS */;
INSERT INTO `quality_defect` VALUES (1,'DEF-TEST-001',1,'IMG_TEST_002','[1, 2, 3]','{\"1\": 50, \"2\": 15, \"3\": 200}','{\"1\": {\"max\": 200, \"min\": 100}, \"2\": {\"max\": 90, \"min\": 30}, \"3\": {\"max\": 600, \"min\": 300}}','2026-05-28 09:14:43',3,1,NULL,0,'2026-05-28 09:14:43',1,'2026-05-28 09:15:04',1),(2,'DEF-TEST-002',1,'IMG_TEST_003','[2]','{\"2\": 10}','{\"2\": {\"max\": 90, \"min\": 30}}','2026-05-28 09:15:18',4,0,'在容差范围内，不影响产品质量',0,'2026-05-28 09:15:18',1,'2026-05-28 09:15:18',1),(3,'DEF-20260528-001',3,'IMG_BAD_001','[1, 2, 3]','{\"1\": 50, \"2\": 95, \"3\": 200}','{\"1\": {\"max\": 200.0, \"min\": 100.0}, \"2\": {\"max\": 90.0, \"min\": 30.0}, \"3\": {\"max\": 600.0, \"min\": 300.0}}','2026-05-28 09:27:53',3,1,NULL,0,'2026-05-28 09:27:53',1,'2026-05-28 09:27:54',1);
/*!40000 ALTER TABLE `quality_defect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_defect_dispose`
--

DROP TABLE IF EXISTS `quality_defect_dispose`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_defect_dispose` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '处置记录ID',
  `defect_id` bigint NOT NULL COMMENT '关联不合格品ID',
  `dispose_plan` tinyint(1) NOT NULL COMMENT '1-重新采集 2-参数调整 3-设备维护 4-接受',
  `operator_id` bigint NOT NULL COMMENT '处置人ID',
  `operate_at` datetime NOT NULL COMMENT '处置时间',
  `result_desc` varchar(500) NOT NULL COMMENT '处置结果说明',
  `verify_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-待验证 1-已验证合格 2-验证不合格',
  `verify_comment` varchar(500) DEFAULT NULL COMMENT '验证备注',
  `verify_at` datetime DEFAULT NULL COMMENT '验证时间',
  PRIMARY KEY (`id`),
  KEY `idx_defect_id` (`defect_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='不合格品处置记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_defect_dispose`
--

LOCK TABLES `quality_defect_dispose` WRITE;
/*!40000 ALTER TABLE `quality_defect_dispose` DISABLE KEYS */;
INSERT INTO `quality_defect_dispose` VALUES (1,1,2,1,'2026-05-28 09:15:04','调整采集参数后重新检测，指标恢复正常',1,'验证通过','2026-05-28 09:15:04'),(2,3,1,1,'2026-05-28 09:27:53','重新采集后合格',1,'验证通过','2026-05-28 09:27:54');
/*!40000 ALTER TABLE `quality_defect_dispose` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_detection_record`
--

DROP TABLE IF EXISTS `quality_detection_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_detection_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '检测记录ID',
  `task_id` bigint NOT NULL COMMENT '关联任务ID',
  `image_id` varchar(200) NOT NULL COMMENT '影像文件名或编号',
  `measured_values` json NOT NULL COMMENT '各指标实测值JSON',
  `is_qualified` tinyint(1) NOT NULL COMMENT '0-不合格 1-合格',
  `exceeded_metrics` json DEFAULT NULL COMMENT '超标指标详情JSON',
  `detected_at` datetime NOT NULL COMMENT '检测时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_qualified` (`task_id`,`is_qualified`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='检测记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_detection_record`
--

LOCK TABLES `quality_detection_record` WRITE;
/*!40000 ALTER TABLE `quality_detection_record` DISABLE KEYS */;
INSERT INTO `quality_detection_record` VALUES (1,1,'IMG_TEST_001','[{\"value\": 150, \"metricId\": 1}, {\"value\": 60, \"metricId\": 2}, {\"value\": 450, \"metricId\": 3}]',1,NULL,'2026-05-28 09:14:07'),(2,1,'IMG_TEST_002','[{\"value\": 50, \"metricId\": 1}, {\"value\": 15, \"metricId\": 2}, {\"value\": 200, \"metricId\": 3}]',1,NULL,'2026-05-28 09:14:07'),(3,3,'IMG_OK_001','[{\"value\": 150, \"metricId\": 1}, {\"value\": 60, \"metricId\": 2}, {\"value\": 450, \"metricId\": 3}]',1,NULL,'2026-05-28 09:27:53'),(4,3,'IMG_BAD_001','[{\"value\": 50, \"metricId\": 1}, {\"value\": 95, \"metricId\": 2}, {\"value\": 200, \"metricId\": 3}]',0,'[1, 2, 3]','2026-05-28 09:27:53');
/*!40000 ALTER TABLE `quality_detection_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_metric`
--

DROP TABLE IF EXISTS `quality_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_metric` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '指标ID',
  `metric_code` varchar(50) NOT NULL COMMENT '指标编号，QM-{序号}',
  `metric_name` varchar(100) NOT NULL COMMENT '指标名称',
  `metric_type` tinyint(1) NOT NULL COMMENT '0-数值型 1-等级型',
  `unit` varchar(50) DEFAULT NULL COMMENT '计量单位',
  `min_value` decimal(10,4) DEFAULT NULL COMMENT '数值型下限',
  `max_value` decimal(10,4) DEFAULT NULL COMMENT '数值型上限',
  `level_desc` text COMMENT '等级描述JSON',
  `importance` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-低 1-中 2-高',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-停用 1-启用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_metric_code` (`metric_code`),
  UNIQUE KEY `uk_metric_name` (`metric_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='质量指标表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_metric`
--

LOCK TABLES `quality_metric` WRITE;
/*!40000 ALTER TABLE `quality_metric` DISABLE KEYS */;
INSERT INTO `quality_metric` VALUES (1,'QM-001','图像亮度均值',0,'gray',100.0000,200.0000,NULL,2,NULL,1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(2,'QM-002','图像对比度',0,'%',30.0000,90.0000,NULL,2,NULL,1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(3,'QM-003','图像分辨率DPI',0,'dpi',300.0000,600.0000,NULL,1,NULL,1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(4,'QM-004','图像清晰度等级',1,NULL,NULL,NULL,NULL,1,NULL,1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(5,'QM-005','测试数值-改',0,'%',0.0000,100.0000,NULL,2,'测试',1,1,'2026-05-28 09:12:27',1,'2026-05-28 09:12:27',1);
/*!40000 ALTER TABLE `quality_metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_task`
--

DROP TABLE IF EXISTS `quality_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_code` varchar(50) NOT NULL COMMENT '任务编号，TASK-{yyyyMMdd}-{序号}',
  `task_name` varchar(200) NOT NULL COMMENT '任务名称',
  `detection_target` varchar(200) NOT NULL COMMENT '检测对象描述',
  `template_id` bigint NOT NULL COMMENT '关联标准模板ID',
  `image_count` int NOT NULL DEFAULT '0' COMMENT '影像总数量',
  `plan_execute_time` datetime DEFAULT NULL COMMENT '计划执行时间',
  `priority` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-低 1-中 2-高',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1-待执行 2-执行中 3-已完成 4-已取消',
  `start_time` datetime DEFAULT NULL COMMENT '实际开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '实际结束时间',
  `qualified_count` int NOT NULL DEFAULT '0' COMMENT '合格影像数',
  `unqualified_count` int NOT NULL DEFAULT '0' COMMENT '不合格影像数',
  `qualified_rate` decimal(5,2) DEFAULT NULL COMMENT '合格率',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_code` (`task_code`),
  KEY `idx_template_status` (`template_id`,`status`),
  KEY `idx_target_status` (`detection_target`(50),`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='检测任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_task`
--

LOCK TABLES `quality_task` WRITE;
/*!40000 ALTER TABLE `quality_task` DISABLE KEYS */;
INSERT INTO `quality_task` VALUES (1,'TASK-20260528-001','测试检测任务','测试产品批次001',1,3,NULL,1,'自动化测试',3,'2026-05-28 09:14:08','2026-05-28 09:14:08',2,0,100.00,0,'2026-05-28 09:14:07',1,'2026-05-28 09:14:07',1),(2,'TASK-20260528-002','测试取消任务','取消批次',1,1,NULL,1,NULL,4,NULL,NULL,0,0,NULL,0,'2026-05-28 09:14:07',1,'2026-05-28 09:14:07',1),(3,'TASK-20260528-003','自动判定测试任务','产品批次TEST',1,3,NULL,1,NULL,2,'2026-05-28 09:27:53',NULL,0,0,NULL,0,'2026-05-28 09:27:53',1,'2026-05-28 09:27:53',1);
/*!40000 ALTER TABLE `quality_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_template`
--

DROP TABLE IF EXISTS `quality_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(50) NOT NULL COMMENT '模板编号，QT-{序号}',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `applicable_scene` varchar(200) DEFAULT NULL COMMENT '适用场景',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-停用 1-启用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='质量标准模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_template`
--

LOCK TABLES `quality_template` WRITE;
/*!40000 ALTER TABLE `quality_template` DISABLE KEYS */;
INSERT INTO `quality_template` VALUES (1,'QT-001','通用视觉检测标准','通用生产线检测',NULL,1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(2,'QT-002','高精度精密件检测标准','精密零部件检测',NULL,1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(4,'QT-003','测试模板-改','测试场景','测试备注',0,1,'2026-05-28 09:12:27',1,'2026-05-28 09:12:27',1);
/*!40000 ALTER TABLE `quality_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_template_metric`
--

DROP TABLE IF EXISTS `quality_template_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_template_metric` (
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `metric_id` bigint NOT NULL COMMENT '指标ID',
  PRIMARY KEY (`template_id`,`metric_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模板指标关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_template_metric`
--

LOCK TABLES `quality_template_metric` WRITE;
/*!40000 ALTER TABLE `quality_template_metric` DISABLE KEYS */;
INSERT INTO `quality_template_metric` VALUES (1,1),(1,2),(1,3),(2,1),(2,2),(2,3),(2,4);
/*!40000 ALTER TABLE `quality_template_metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父节点ID，0表示根',
  `menu_name` varchar(100) NOT NULL COMMENT '菜单名称',
  `menu_type` tinyint(1) NOT NULL COMMENT '0-目录 1-菜单 2-按钮',
  `path` varchar(200) DEFAULT NULL COMMENT '路由路径',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'系统管理',0,'/system',NULL,'Setting',1,1),(2,1,'用户管理',1,'/system/users','system:user:list','User',1,1),(3,1,'角色管理',1,'/system/roles','system:role:list','UserFilled',2,1),(4,1,'菜单管理',1,'/system/menus','system:menu:list','Menu',3,1),(5,0,'质量管理',0,'/quality',NULL,'DataAnalysis',2,1),(6,5,'质量指标',1,'/quality/metrics','quality:metric:list','DocumentChecked',1,1),(7,5,'标准模板',1,'/quality/templates','quality:template:list','Files',2,1),(8,5,'检测任务',1,'/quality/tasks','quality:task:list','Tickets',3,1),(9,5,'一致性分析',1,'/quality/analysis','quality:analysis:view','TrendCharts',4,1),(10,5,'不合格品管理',1,'/quality/defects','quality:defect:list','Warning',5,1),(11,0,'统计报表',0,'/stats',NULL,'Histogram',3,1),(12,11,'质量统计看板',1,'/stats/dashboard','stats:dashboard:view','Odometer',1,1),(13,11,'趋势报表',1,'/stats/trend','stats:trend:view','TrendCharts',2,1),(14,2,'用户查询',2,NULL,'system:user:list',NULL,1,1),(15,2,'用户新增',2,NULL,'system:user:add',NULL,2,1),(16,2,'用户修改',2,NULL,'system:user:edit',NULL,3,1),(17,2,'用户删除',2,NULL,'system:user:delete',NULL,4,1),(18,3,'角色查询',2,NULL,'system:role:list',NULL,1,1),(19,3,'角色新增',2,NULL,'system:role:add',NULL,2,1),(20,3,'角色修改',2,NULL,'system:role:edit',NULL,3,1),(21,3,'角色删除',2,NULL,'system:role:delete',NULL,4,1),(22,4,'菜单查询',2,NULL,'system:menu:list',NULL,1,1),(23,4,'菜单新增',2,NULL,'system:menu:add',NULL,2,1),(24,4,'菜单修改',2,NULL,'system:menu:edit',NULL,3,1),(25,4,'菜单删除',2,NULL,'system:menu:delete',NULL,4,1),(26,6,'指标查询',2,NULL,'quality:metric:list',NULL,1,1),(27,6,'指标新增',2,NULL,'quality:metric:add',NULL,2,1),(28,6,'指标修改',2,NULL,'quality:metric:edit',NULL,3,1),(29,6,'指标删除',2,NULL,'quality:metric:delete',NULL,4,1),(30,7,'模板查询',2,NULL,'quality:template:list',NULL,1,1),(31,7,'模板新增',2,NULL,'quality:template:add',NULL,2,1),(32,7,'模板修改',2,NULL,'quality:template:edit',NULL,3,1),(33,7,'模板删除',2,NULL,'quality:template:delete',NULL,4,1),(34,8,'任务查询',2,NULL,'quality:task:list',NULL,1,1),(35,8,'任务新增',2,NULL,'quality:task:add',NULL,2,1),(36,8,'任务修改',2,NULL,'quality:task:edit',NULL,3,1),(37,10,'不合格品查询',2,NULL,'quality:defect:list',NULL,1,1),(38,10,'不合格品处置',2,NULL,'quality:defect:dispose',NULL,2,1),(39,10,'不合格品验证',2,NULL,'quality:defect:verify',NULL,3,1),(44,0,'智能体管理',1,'/agent',NULL,'robot',5,1),(45,44,'查询智能体',2,NULL,'agent:list',NULL,1,1),(46,44,'注册智能体',2,NULL,'agent:add',NULL,2,1),(47,44,'编辑智能体',2,NULL,'agent:edit',NULL,3,1),(48,44,'删除智能体',2,NULL,'agent:delete',NULL,4,1),(49,44,'调度任务',2,NULL,'agent:dispatch',NULL,5,1);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色标识',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'系统管理员','SUPER_ADMIN','拥有全部权限',1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(2,'质量标准工程师','QUALITY_ENG','负责质量标准定义与检测任务配置',1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(3,'检测操作员','OPERATOR','负责执行检测任务，查看检测结果',1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(4,'管理人员','MANAGER','查看质量趋势统计，掌握整体管控状态',1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 09:43:40',NULL),(7,'测试角色-改','TEST_ROLE','测试',1,1,'2026-05-28 09:10:51',1,'2026-05-28 09:10:51',1);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,44),(1,45),(1,46),(1,47),(1,48),(1,49);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '密码（BCrypt）',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `dept` varchar(100) DEFAULT NULL COMMENT '部门',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$xs5dJ5Unjq/FiHEZIwa7u.tNkbREm/PJ6S8Ze.U4utqsetzmUfA7O','系统管理员','13800138000','系统管理部',1,0,'2026-05-27 09:43:40',NULL,'2026-05-27 10:11:06',NULL),(4,'testop01','$2a$10$6HLZ0zBiuZFHsd.7qnb9Y.Li3pAQLf8mCTEcrvKhmpi9AoNUJBTpC','测试操作员-改','13900001111',NULL,1,1,'2026-05-28 09:10:51',1,'2026-05-28 09:10:51',4);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
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
-- Dumping routines for database 'vision_quality_consistency_control_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:14:04
