-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: industrial_imaging_data_platform_dev
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
-- Table structure for table `datasource_config`
--

DROP TABLE IF EXISTS `datasource_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datasource_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据源ID',
  `datasource_code` varchar(30) NOT NULL COMMENT '数据源编号(自动)',
  `datasource_name` varchar(100) NOT NULL COMMENT '数据源名称',
  `datasource_type` varchar(20) NOT NULL COMMENT '类型:DEVICE/FILE_SERVER/DATABASE/OBJECT_STORAGE',
  `host` varchar(255) NOT NULL COMMENT '连接地址',
  `port` int NOT NULL COMMENT '端口',
  `auth_type` varchar(20) NOT NULL DEFAULT 'NONE' COMMENT '认证方式:NONE/PASSWORD/KEY',
  `auth_username` varchar(100) DEFAULT NULL COMMENT '认证账号',
  `auth_password` varchar(500) DEFAULT NULL COMMENT '认证密码(加密存储)',
  `auth_key` text COMMENT '密钥(加密存储)',
  `data_format` varchar(20) NOT NULL COMMENT '数据格式:JPEG/PNG/RAW/MP4/OTHER',
  `ext_config` json DEFAULT NULL COMMENT '扩展配置JSON',
  `owner_id` bigint DEFAULT NULL COMMENT '负责人用户ID',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0停用1启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_datasource_code` (`datasource_code`),
  UNIQUE KEY `uk_datasource_name` (`datasource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datasource_config`
--

LOCK TABLES `datasource_config` WRITE;
/*!40000 ALTER TABLE `datasource_config` DISABLE KEYS */;
INSERT INTO `datasource_config` VALUES (1,'DS001','FTP生产线A','FTP','192.168.1.10',21,'PASSWORD','ftpuser','ftp123',NULL,'IMAGE',NULL,NULL,1,0,'生产线A影像FTP数据源','2026-05-29 10:46:08',NULL,'2026-05-29 10:46:08',NULL),(2,'DS002','SFTP质检中心','SFTP','192.168.1.20',22,'PASSWORD','sftp_qc','sftp456',NULL,'IMAGE',NULL,NULL,1,0,'质检中心SFTP数据源','2026-05-29 10:46:08',NULL,'2026-05-29 10:46:08',NULL),(3,'DS003','本地存储','LOCAL','127.0.0.1',0,'NONE',NULL,NULL,NULL,'IMAGE',NULL,NULL,1,0,'本地文件系统数据源','2026-05-29 10:46:08',NULL,'2026-05-29 10:46:08',NULL),(4,'DS004','离线导入','FTP','192.168.2.50',21,'PASSWORD','offline','offline789',NULL,'RAW',NULL,NULL,0,0,'离线批量导入数据源','2026-05-29 10:46:08',NULL,'2026-05-29 10:46:08',NULL);
/*!40000 ALTER TABLE `datasource_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datasource_conn_log`
--

DROP TABLE IF EXISTS `datasource_conn_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datasource_conn_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `datasource_id` bigint NOT NULL COMMENT '数据源ID',
  `result` tinyint NOT NULL COMMENT '检测结果:0失败1成功',
  `error_msg` text COMMENT '失败原因',
  `cost_time` int DEFAULT '0' COMMENT '耗时(ms)',
  `tested_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
  `tested_by` bigint NOT NULL DEFAULT '0' COMMENT '执行人(0=系统)',
  PRIMARY KEY (`id`),
  KEY `idx_datasource_id` (`datasource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源连接测试日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datasource_conn_log`
--

LOCK TABLES `datasource_conn_log` WRITE;
/*!40000 ALTER TABLE `datasource_conn_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `datasource_conn_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingest_record`
--

DROP TABLE IF EXISTS `ingest_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingest_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_code` varchar(30) NOT NULL COMMENT '记录编号',
  `task_id` bigint NOT NULL COMMENT '接入任务ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称快照',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '完成时间',
  `cost_seconds` int DEFAULT '0' COMMENT '耗时(秒)',
  `ingest_count` int DEFAULT '0' COMMENT '接入文件数',
  `data_size_bytes` bigint DEFAULT '0' COMMENT '数据大小(字节)',
  `execute_status` varchar(20) NOT NULL DEFAULT 'RUNNING' COMMENT '状态:SUCCESS/PARTIAL/FAILED',
  `fail_reason` text COMMENT '失败原因',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_code` (`record_code`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingest_record`
--

LOCK TABLES `ingest_record` WRITE;
/*!40000 ALTER TABLE `ingest_record` DISABLE KEYS */;
INSERT INTO `ingest_record` VALUES (1,'IR001',1,'每30分钟采集线A影像','2026-05-23 10:46:52','2026-05-23 10:47:37',45,120,524288000,'SUCCESS',NULL,'2026-05-29 10:46:52'),(2,'IR002',2,'每小时质检采集','2026-05-23 10:46:52','2026-05-23 10:48:52',120,85,376832000,'SUCCESS',NULL,'2026-05-29 10:46:52'),(3,'IR003',1,'每30分钟采集线A影像','2026-05-24 10:46:52','2026-05-24 10:47:44',52,135,609734656,'SUCCESS',NULL,'2026-05-29 10:46:52'),(4,'IR004',3,'每日本地归档','2026-05-24 10:46:52','2026-05-24 10:51:52',300,512,2147483648,'SUCCESS',NULL,'2026-05-29 10:46:52'),(5,'IR005',2,'每小时质检采集','2026-05-25 10:46:52','2026-05-25 10:48:30',98,92,419430400,'SUCCESS',NULL,'2026-05-29 10:46:52'),(6,'IR006',1,'每30分钟采集线A影像','2026-05-25 10:46:52','2026-05-25 10:47:30',38,108,471859200,'FAILED',NULL,'2026-05-29 10:46:52'),(7,'IR007',1,'每30分钟采集线A影像','2026-05-26 10:46:52','2026-05-26 10:47:33',41,142,661651456,'SUCCESS',NULL,'2026-05-29 10:46:52'),(8,'IR008',2,'每小时质检采集','2026-05-26 10:46:52','2026-05-26 10:48:47',115,78,335544320,'SUCCESS',NULL,'2026-05-29 10:46:52'),(9,'IR009',3,'每日本地归档','2026-05-26 10:46:52','2026-05-26 10:51:32',280,480,2013265920,'SUCCESS',NULL,'2026-05-29 10:46:52'),(10,'IR010',1,'每30分钟采集线A影像','2026-05-27 10:46:52','2026-05-27 10:47:47',55,158,734003200,'SUCCESS',NULL,'2026-05-29 10:46:52'),(11,'IR011',2,'每小时质检采集','2026-05-27 10:46:52','2026-05-27 10:48:40',108,95,440401920,'SUCCESS',NULL,'2026-05-29 10:46:52'),(12,'IR012',1,'每30分钟采集线A影像','2026-05-28 10:46:52','2026-05-28 10:47:41',49,162,754974720,'SUCCESS',NULL,'2026-05-29 10:46:52'),(13,'IR013',3,'每日本地归档','2026-05-28 10:46:52','2026-05-28 10:52:12',320,540,2264924160,'SUCCESS',NULL,'2026-05-29 10:46:52'),(14,'IR014',1,'每30分钟采集线A影像','2026-05-29 08:46:52','2026-05-29 08:47:39',47,145,673185792,'SUCCESS',NULL,'2026-05-29 10:46:52'),(15,'IR015',2,'每小时质检采集','2026-05-29 09:46:52',NULL,0,0,0,'RUNNING',NULL,'2026-05-29 10:46:52');
/*!40000 ALTER TABLE `ingest_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingest_task`
--

DROP TABLE IF EXISTS `ingest_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingest_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_code` varchar(30) NOT NULL COMMENT '任务编号',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `datasource_id` bigint NOT NULL COMMENT '数据源ID',
  `ingest_type` varchar(20) NOT NULL COMMENT '接入方式:REALTIME/SCHEDULED',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT '调度表达式',
  `filter_file_types` varchar(200) DEFAULT NULL COMMENT '文件类型过滤(JSON数组)',
  `filter_file_pattern` varchar(255) DEFAULT NULL COMMENT '文件名规则',
  `filter_start_time` datetime DEFAULT NULL COMMENT '时间过滤起始',
  `filter_end_time` datetime DEFAULT NULL COMMENT '时间过滤结束',
  `storage_dir` varchar(500) NOT NULL COMMENT '存储目录',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0停用1启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_code` (`task_code`),
  UNIQUE KEY `uk_task_name` (`task_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingest_task`
--

LOCK TABLES `ingest_task` WRITE;
/*!40000 ALTER TABLE `ingest_task` DISABLE KEYS */;
INSERT INTO `ingest_task` VALUES (1,'IT001','每30分钟采集线A影像',1,'SCHEDULED','0 0/30 * * * ?','jpg,png,tiff',NULL,NULL,NULL,'/data/raw/lineA',1,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(2,'IT002','每小时质检采集',2,'SCHEDULED','0 0 * * * ?','jpg,bmp',NULL,NULL,NULL,'/data/raw/quality',1,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(3,'IT003','每日本地归档',3,'SCHEDULED','0 0 2 * * ?','*',NULL,NULL,NULL,'/data/raw/local',1,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(4,'IT004','离线批量导入',4,'MANUAL',NULL,'zip,tar',NULL,NULL,NULL,'/data/raw/offline',0,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL);
/*!40000 ALTER TABLE `ingest_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_execution`
--

DROP TABLE IF EXISTS `process_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_execution` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '执行ID',
  `exec_code` varchar(30) NOT NULL COMMENT '执行编号',
  `task_id` bigint NOT NULL COMMENT '处理任务ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称快照',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `total_count` int DEFAULT '0' COMMENT '处理文件总数',
  `success_count` int DEFAULT '0' COMMENT '成功数',
  `fail_count` int DEFAULT '0' COMMENT '失败数',
  `output_size_bytes` bigint DEFAULT '0' COMMENT '输出大小(字节)',
  `execute_status` varchar(20) NOT NULL DEFAULT 'RUNNING' COMMENT '状态:RUNNING/COMPLETED/FAILED/TERMINATED',
  `fail_file_list` json DEFAULT NULL COMMENT '失败文件清单',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exec_code` (`exec_code`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='处理执行记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_execution`
--

LOCK TABLES `process_execution` WRITE;
/*!40000 ALTER TABLE `process_execution` DISABLE KEYS */;
INSERT INTO `process_execution` VALUES (1,'PE001',1,'图像压缩任务','2026-05-23 10:46:52','2026-05-23 10:49:52',120,118,2,209715200,'SUCCESS',NULL,'2026-05-29 10:46:52'),(2,'PE002',2,'格式转换任务','2026-05-24 10:46:52','2026-05-24 10:50:52',85,85,0,157286400,'SUCCESS',NULL,'2026-05-29 10:46:52'),(3,'PE003',3,'元数据提取','2026-05-24 10:46:52','2026-05-24 10:48:22',512,510,2,5242880,'SUCCESS',NULL,'2026-05-29 10:46:52'),(4,'PE004',1,'图像压缩任务','2026-05-25 10:46:52','2026-05-25 10:49:37',135,135,0,241172480,'SUCCESS',NULL,'2026-05-29 10:46:52'),(5,'PE005',1,'图像压缩任务','2026-05-26 10:46:52','2026-05-26 10:50:07',142,140,2,251658240,'SUCCESS',NULL,'2026-05-29 10:46:52'),(6,'PE006',2,'格式转换任务','2026-05-27 10:46:52','2026-05-27 10:51:12',92,90,2,170917888,'SUCCESS',NULL,'2026-05-29 10:46:52'),(7,'PE007',3,'元数据提取','2026-05-27 10:46:52','2026-05-27 10:48:17',480,480,0,5033164,'SUCCESS',NULL,'2026-05-29 10:46:52'),(8,'PE008',1,'图像压缩任务','2026-05-28 10:46:52','2026-05-28 10:49:47',162,160,2,289406976,'SUCCESS',NULL,'2026-05-29 10:46:52'),(9,'PE009',4,'图像去噪处理','2026-05-29 07:46:52',NULL,45,40,2,0,'RUNNING',NULL,'2026-05-29 10:46:52');
/*!40000 ALTER TABLE `process_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_task`
--

DROP TABLE IF EXISTS `process_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_code` varchar(30) NOT NULL COMMENT '任务编号',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `input_dir` varchar(500) NOT NULL COMMENT '输入目录',
  `process_type` varchar(30) NOT NULL COMMENT '处理类型:IMAGE_COMPRESS/FORMAT_CONVERT/RESOLUTION_RESIZE/BATCH_RENAME/QUALITY_FILTER',
  `process_params` json NOT NULL COMMENT '处理参数JSON',
  `output_dir` varchar(500) NOT NULL COMMENT '输出目录',
  `execute_type` varchar(20) NOT NULL DEFAULT 'MANUAL' COMMENT '执行方式:MANUAL/SCHEDULED',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT '调度表达式',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0停用1启用2执行中3已终止',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_code` (`task_code`),
  UNIQUE KEY `uk_task_name` (`task_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='处理任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_task`
--

LOCK TABLES `process_task` WRITE;
/*!40000 ALTER TABLE `process_task` DISABLE KEYS */;
INSERT INTO `process_task` VALUES (1,'PT001','图像压缩任务','/data/raw/lineA','IMAGE_COMPRESS','{\"format\": \"jpg\", \"quality\": 75}','/data/compressed/lineA','SCHEDULED','0 0/15 * * * ?',1,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(2,'PT002','格式转换任务','/data/raw/quality','FORMAT_CONVERT','{\"dpi\": 300, \"targetFormat\": \"tiff\"}','/data/converted/quality','SCHEDULED','0 30 * * * ?',1,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(3,'PT003','元数据提取','/data/raw','METADATA_EXTRACT','{\"extractGps\": false, \"extractExif\": true}','/data/metadata','SCHEDULED','0 0 1 * * ?',1,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(4,'PT004','图像去噪处理','/data/raw/lineA','IMAGE_ENHANCE','{\"sigma\": 1.5, \"method\": \"gaussian\"}','/data/enhanced','MANUAL',NULL,0,0,'2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL);
/*!40000 ALTER TABLE `process_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage_clean_log`
--

DROP TABLE IF EXISTS `storage_clean_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_clean_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `rule_id` bigint NOT NULL COMMENT '清理规则ID',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称快照',
  `execute_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `execute_type` varchar(20) NOT NULL COMMENT '触发方式:MANUAL/SCHEDULED',
  `deleted_count` int DEFAULT '0' COMMENT '删除/归档文件数',
  `freed_bytes` bigint DEFAULT '0' COMMENT '释放空间(字节)',
  `execute_status` varchar(20) NOT NULL DEFAULT 'SUCCESS' COMMENT '执行状态:SUCCESS/FAILED',
  `error_msg` text COMMENT '错误信息',
  `executed_by` bigint DEFAULT '0' COMMENT '执行人(0=系统)',
  PRIMARY KEY (`id`),
  KEY `idx_rule_id` (`rule_id`),
  KEY `idx_execute_time` (`execute_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据清理日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage_clean_log`
--

LOCK TABLES `storage_clean_log` WRITE;
/*!40000 ALTER TABLE `storage_clean_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `storage_clean_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage_clean_rule`
--

DROP TABLE IF EXISTS `storage_clean_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_clean_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `target_dir` varchar(500) NOT NULL COMMENT '目标清理目录',
  `condition_type` varchar(30) NOT NULL COMMENT '清理条件:LAST_ACCESS_DAYS/BEFORE_DATE/FILE_SIZE',
  `condition_value` json NOT NULL COMMENT '条件值JSON',
  `execute_type` varchar(20) NOT NULL DEFAULT 'MANUAL' COMMENT '执行方式:MANUAL/SCHEDULED',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT '调度表达式',
  `after_action` varchar(20) NOT NULL DEFAULT 'DELETE' COMMENT '清理后操作:DELETE/ARCHIVE',
  `archive_dir` varchar(500) DEFAULT NULL COMMENT '归档目录',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0停用1启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_name` (`rule_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据清理规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage_clean_rule`
--

LOCK TABLES `storage_clean_rule` WRITE;
/*!40000 ALTER TABLE `storage_clean_rule` DISABLE KEYS */;
INSERT INTO `storage_clean_rule` VALUES (1,'原始数据30天清理','/data/raw','EXPIRE_DAYS','{\"days\": 30}','SCHEDULED','0 0 3 * * ?','DELETE',NULL,1,0,'保留30天原始采集数据','2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(2,'压缩数据90天归档','/data/compressed','EXPIRE_DAYS','{\"days\": 90}','SCHEDULED','0 0 4 * * ?','ARCHIVE',NULL,1,0,'90天后归档压缩数据','2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(3,'大文件容量限制','/data/raw','STORAGE_LIMIT','{\"maxGb\": 500}','SCHEDULED','0 0 2 * * ?','DELETE',NULL,1,0,'目录超500GB时清理最旧文件','2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL),(4,'临时文件每日清理','/tmp/ingest','EXPIRE_DAYS','{\"days\": 7}','SCHEDULED','0 0 0 * * ?','DELETE',NULL,1,0,'每天清理7天前临时文件','2026-05-29 10:46:27',NULL,'2026-05-29 10:46:27',NULL);
/*!40000 ALTER TABLE `storage_clean_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage_dir_stat`
--

DROP TABLE IF EXISTS `storage_dir_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_dir_stat` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dir_path` varchar(500) NOT NULL COMMENT '目录路径',
  `size_bytes` bigint NOT NULL DEFAULT '0' COMMENT '占用大小(字节)',
  `file_count` int NOT NULL DEFAULT '0' COMMENT '文件数量',
  `last_modified_at` datetime DEFAULT NULL COMMENT '最后修改时间',
  `snapshot_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
  PRIMARY KEY (`id`),
  KEY `idx_snapshot_time` (`snapshot_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储目录统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage_dir_stat`
--

LOCK TABLES `storage_dir_stat` WRITE;
/*!40000 ALTER TABLE `storage_dir_stat` DISABLE KEYS */;
/*!40000 ALTER TABLE `storage_dir_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage_snapshot`
--

DROP TABLE IF EXISTS `storage_snapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_snapshot` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '快照ID',
  `total_bytes` bigint NOT NULL DEFAULT '0' COMMENT '总容量(字节)',
  `used_bytes` bigint NOT NULL DEFAULT '0' COMMENT '已用(字节)',
  `free_bytes` bigint NOT NULL DEFAULT '0' COMMENT '可用(字节)',
  `usage_rate` decimal(5,2) DEFAULT '0.00' COMMENT '使用率(%)',
  `snapshot_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
  PRIMARY KEY (`id`),
  KEY `idx_snapshot_time` (`snapshot_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储空间快照';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage_snapshot`
--

LOCK TABLES `storage_snapshot` WRITE;
/*!40000 ALTER TABLE `storage_snapshot` DISABLE KEYS */;
/*!40000 ALTER TABLE `storage_snapshot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'M' COMMENT '类型:M目录C菜单F按钮',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `visible` tinyint NOT NULL DEFAULT '1' COMMENT '是否显示:0否1是',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0停用1启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'基础管理','M','/system',NULL,NULL,'#',1,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(2,1,'用户管理','C','user',NULL,'system:user:list','#',1,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(3,1,'角色管理','C','role',NULL,'system:role:list','#',2,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(4,1,'菜单管理','C','menu',NULL,'system:menu:list','#',3,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(5,1,'操作日志','C','oper-log',NULL,'logs:operation:list','#',4,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(10,0,'数据源管理','C','/datasource',NULL,'datasource:config:list','#',2,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(11,10,'新增数据源','F','',NULL,'datasource:config:add','#',1,1,1,0,'2026-06-01 16:13:09',NULL,'2026-06-01 16:13:09',NULL),(12,10,'编辑数据源','F','',NULL,'datasource:config:edit','#',2,1,1,0,'2026-06-01 16:13:09',NULL,'2026-06-01 16:13:09',NULL),(13,10,'删除数据源','F','',NULL,'datasource:config:delete','#',3,1,1,0,'2026-06-01 16:13:09',NULL,'2026-06-01 16:13:09',NULL),(14,10,'测试连接','F','',NULL,'datasource:config:test','#',4,1,1,0,'2026-06-01 16:13:09',NULL,'2026-06-01 16:13:09',NULL),(15,10,'状态切换','F','',NULL,'datasource:config:status','#',5,1,1,0,'2026-06-01 16:13:09',NULL,'2026-06-01 16:13:09',NULL),(20,0,'数据接入管理','M','/ingest',NULL,NULL,'#',3,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(21,20,'接入任务','C','task',NULL,'ingest:task:list','#',1,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(22,20,'接入记录','C','record',NULL,'ingest:record:list','#',2,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(30,0,'数据处理管理','M','/process',NULL,NULL,'#',4,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(31,30,'处理任务','C','task',NULL,'process:task:list','#',1,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(32,30,'执行监控','C','monitor',NULL,'process:execution:view','#',2,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(33,30,'处理结果','C','result',NULL,'process:result:list','#',3,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(40,0,'数据存储管理','M','/storage',NULL,NULL,'#',5,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(41,40,'存储总览','C','overview',NULL,'storage:overview:view','#',1,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(42,40,'清理规则','C','clean-rule',NULL,'storage:clean:list','#',2,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(43,40,'清理日志','C','clean-log',NULL,'storage:clean:log','#',3,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(50,0,'统计分析','M','/stats',NULL,NULL,'#',6,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(51,50,'数据总览','C','overview',NULL,'stats:overview:view','#',1,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(52,50,'处理统计','C','process',NULL,'stats:process:view','#',2,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(53,50,'趋势分析','C','trend',NULL,'stats:analysis:view','#',3,1,1,0,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` tinyint DEFAULT '0' COMMENT '业务类型:0其他1新增2修改3删除4查询',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` tinyint DEFAULT '0' COMMENT '操作类别:0其他1后台用户',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) DEFAULT '' COMMENT '操作地址',
  `oper_param` text COMMENT '请求参数',
  `json_result` text COMMENT '返回参数',
  `status` tinyint DEFAULT '0' COMMENT '操作状态:0正常1异常',
  `error_msg` text COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间(ms)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (1,'数据源管理',1,'addDatasource','POST',1,'admin','/api/datasource','127.0.0.1','{\"name\":\"FTP生产线A\"}','{\"code\":200}',0,NULL,'2026-05-24 10:47:14',0),(2,'采集任务',1,'addIngestTask','POST',1,'admin','/api/ingest/tasks','127.0.0.1','{\"taskName\":\"每30分钟采集线A影像\"}','{\"code\":200}',0,NULL,'2026-05-25 10:47:14',0),(3,'处理任务',2,'updateProcessTask','PUT',1,'admin','/api/process/tasks/1','127.0.0.1','{\"status\":1}','{\"code\":200}',0,NULL,'2026-05-26 10:47:14',0),(4,'采集任务',5,'triggerIngestTask','POST',1,'admin','/api/ingest/tasks/1/trigger','127.0.0.1','{}','{\"code\":200}',0,NULL,'2026-05-27 10:47:14',0),(5,'系统用户',1,'addUser','POST',1,'admin','/api/system/users','127.0.0.1','{\"username\":\"zhangsan\"}','{\"code\":200}',0,NULL,'2026-05-28 10:47:14',0),(6,'存储清理',5,'executeClean','POST',1,'admin','/api/storage/clean/execute','127.0.0.1','{\"ruleId\":1}','{\"code\":200}',0,NULL,'2026-05-29 04:47:14',0),(7,'数据源管理',6,'testDatasourceConn','POST',1,'admin','/api/datasource/1/test','127.0.0.1','{}','{\"code\":200}',0,NULL,'2026-05-29 09:47:14',0);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色标识',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0停用1启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,1,0,NULL,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(2,'数据工程师','data_engineer',2,1,0,NULL,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(3,'业务分析师','analyst',3,1,0,NULL,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL),(4,'管理人员','manager',4,1,0,NULL,'2026-05-27 10:02:14',NULL,'2026-05-27 10:02:14',NULL);
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
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,20),(1,21),(1,22),(1,30),(1,31),(1,32),(1,33),(1,40),(1,41),(1,42),(1,43),(1,50),(1,51),(1,52),(1,53);
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
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码(BCrypt)',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0停用1启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除:0否1是',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$nRttHrXGoxfCcNYhWLeZu.FwLfiuLVzAB8KyOdJorWnChYdZNztQW','超级管理员',NULL,NULL,NULL,1,0,NULL,'2026-05-27 10:02:14',NULL,'2026-05-27 10:10:20',NULL),(2,'zhangsan','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE36ujXolIgLDKKiW','张三','zhangsan@example.com','13800001111',NULL,1,0,'数据采集工程师','2026-05-29 10:47:14',NULL,'2026-05-29 10:47:14',NULL),(3,'lisi','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE36ujXolIgLDKKiW','李四','lisi@example.com','13800002222',NULL,1,0,'数据分析师','2026-05-29 10:47:14',NULL,'2026-05-29 10:47:14',NULL),(4,'wangwu','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE36ujXolIgLDKKiW','王五','wangwu@example.com','13800003333',NULL,0,0,'已停用用户','2026-05-29 10:47:14',NULL,'2026-05-29 10:47:14',NULL);
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
INSERT INTO `sys_user_role` VALUES (1,1),(2,2),(3,3);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'industrial_imaging_data_platform_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:10:04
