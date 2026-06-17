-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: vision_defect_traceability_dev
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
-- Table structure for table `alert_record`
--

DROP TABLE IF EXISTS `alert_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_id` bigint NOT NULL,
  `alert_content` text NOT NULL,
  `alert_time` datetime NOT NULL,
  `handle_status` tinyint NOT NULL DEFAULT '0' COMMENT '0未处理 1已处理',
  `handle_remark` varchar(500) DEFAULT NULL,
  `handle_by` varchar(50) DEFAULT NULL,
  `handle_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rule` (`rule_id`),
  KEY `idx_time` (`alert_time`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_record`
--

LOCK TABLES `alert_record` WRITE;
/*!40000 ALTER TABLE `alert_record` DISABLE KEYS */;
INSERT INTO `alert_record` VALUES (1,1,'A1号产线合格率下降至72.3%，低于预警阈值80%','2026-05-28 15:29:50',0,NULL,NULL,NULL,'2026-05-28 15:29:50'),(2,2,'A1号产线缺陷数量达到13件，超过预警阈值10件','2026-05-28 14:59:50',0,NULL,NULL,NULL,'2026-05-28 14:59:50'),(3,3,'A2号产线合格率下降至81.5%，低于预警阈值85%','2026-05-28 13:59:50',0,NULL,NULL,NULL,'2026-05-28 13:59:50'),(4,1,'A1号产线合格率下降至68.0%，低于预警阈值80%','2026-05-27 14:59:50',1,'已排查产线设备，调整检测参数','1','2026-05-27 16:59:50','2026-05-27 14:59:50'),(5,4,'B1号产线裂纹缺陷数量达到7件，超过预警阈值5件','2026-05-27 12:59:50',1,'已停线检查，更换模具后恢复生产','1','2026-05-27 13:59:50','2026-05-27 12:59:50'),(6,2,'A1号产线缺陷数量达到15件，超过预警阈值10件','2026-05-27 11:59:50',1,'临时调配人员加强质检','1','2026-05-27 12:59:50','2026-05-27 11:59:50'),(7,3,'A2号产线合格率下降至79.2%，低于预警阈值85%','2026-05-26 13:59:50',1,'检查原材料批次，更换合格原料后恢复','1','2026-05-26 14:59:50','2026-05-26 13:59:50'),(8,1,'A1号产线合格率下降至75.6%，低于预警阈值80%','2026-05-25 13:59:50',1,'调整设备参数，加强员工培训','1','2026-05-25 15:59:50','2026-05-25 13:59:50'),(9,4,'B1号产线裂纹缺陷数量达到6件，超过预警阈值5件','2026-05-24 13:59:50',1,'更换刀具后缺陷率恢复正常','1','2026-05-24 15:59:50','2026-05-24 13:59:50'),(10,2,'A1号产线缺陷数量达到12件，超过预警阈值10件','2026-05-23 13:59:50',1,'排查设备后找到振动异常源并修复','1','2026-05-23 14:59:50','2026-05-23 13:59:50'),(11,3,'A2号产线合格率下降至82.1%，低于预警阈值85%','2026-05-22 13:59:50',1,'环境温湿度调整后恢复正常','1','2026-05-22 14:59:50','2026-05-22 13:59:50'),(12,1,'A1号产线合格率下降至71.0%，低于预警阈值80%','2026-05-21 13:59:50',1,'更换视觉检测镜头后精度恢复','1','2026-05-21 14:59:50','2026-05-21 13:59:50');
/*!40000 ALTER TABLE `alert_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_rule`
--

DROP TABLE IF EXISTS `alert_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(100) NOT NULL,
  `line_id` bigint DEFAULT NULL,
  `condition_type` int NOT NULL COMMENT '1:合格率低于 2:缺陷数超过',
  `threshold` decimal(10,4) NOT NULL,
  `stat_cycle` int NOT NULL DEFAULT '60' COMMENT '统计周期(分钟)',
  `alert_level` tinyint NOT NULL DEFAULT '1' COMMENT '1普通 2重要 3紧急',
  `notify_user_ids` text,
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(50) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_rule`
--

LOCK TABLES `alert_rule` WRITE;
/*!40000 ALTER TABLE `alert_rule` DISABLE KEYS */;
INSERT INTO `alert_rule` VALUES (1,'合格率预警',4,1,0.8000,1,2,NULL,1,0,'2026-05-28 15:34:19','1','2026-05-28 15:34:19',NULL),(2,'缺陷数超量预警',1,2,10.0000,2,3,NULL,1,0,'2026-05-28 15:59:01','1','2026-05-28 15:59:01',NULL),(3,'B线合格率预警',2,1,0.8500,1,2,NULL,1,0,'2026-05-28 15:59:01','1','2026-05-28 15:59:01',NULL),(4,'裂纹缺陷预警',3,2,5.0000,1,3,NULL,1,0,'2026-05-28 15:59:01','1','2026-05-28 15:59:01',NULL);
/*!40000 ALTER TABLE `alert_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `defect_category`
--

DROP TABLE IF EXISTS `defect_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `defect_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `level` int DEFAULT NULL COMMENT '缺陷等级 1轻微 2一般 3严重',
  `description` varchar(255) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `defect_category`
--

LOCK TABLES `defect_category` WRITE;
/*!40000 ALTER TABLE `defect_category` DISABLE KEYS */;
INSERT INTO `defect_category` VALUES (1,'DEF-SCRATCH','划痕',NULL,NULL,1,0,NULL,'2026-05-27 10:59:46',NULL,NULL),(2,'DEF-DENT','凹坑',NULL,NULL,1,0,NULL,'2026-05-27 10:59:46',NULL,NULL),(3,'DEF-CRACK','裂纹',NULL,NULL,1,0,NULL,'2026-05-27 10:59:46',NULL,NULL),(4,'DEF-STAIN','污渍',NULL,NULL,1,0,NULL,'2026-05-27 10:59:46',NULL,NULL),(5,'DEF-BUBBLE','气泡',NULL,NULL,1,0,NULL,'2026-05-27 10:59:46',NULL,NULL),(6,'DEF-MISSING','缺料',NULL,NULL,1,0,NULL,'2026-05-27 10:59:46',NULL,NULL);
/*!40000 ALTER TABLE `defect_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `defect_image`
--

DROP TABLE IF EXISTS `defect_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `defect_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` bigint NOT NULL,
  `image_url` varchar(500) NOT NULL,
  `annotations` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_record` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `defect_image`
--

LOCK TABLES `defect_image` WRITE;
/*!40000 ALTER TABLE `defect_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `defect_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `defect_record`
--

DROP TABLE IF EXISTS `defect_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `defect_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `serial_no` varchar(100) NOT NULL COMMENT '序列号',
  `batch_no` varchar(100) NOT NULL COMMENT '批次号',
  `line_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `level` tinyint DEFAULT NULL COMMENT '1轻微 2一般 3严重',
  `result` tinyint NOT NULL COMMENT '0缺陷 1合格',
  `shift` varchar(10) DEFAULT NULL COMMENT 'A/B/C',
  `detect_time` datetime NOT NULL,
  `dispose_status` tinyint NOT NULL DEFAULT '0' COMMENT '0待处理 1已处理',
  `dispose_remark` varchar(500) DEFAULT NULL,
  `dispose_by` varchar(50) DEFAULT NULL,
  `dispose_at` datetime DEFAULT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_batch` (`batch_no`),
  KEY `idx_serial` (`serial_no`),
  KEY `idx_time` (`detect_time`),
  KEY `idx_line` (`line_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `defect_record`
--

LOCK TABLES `defect_record` WRITE;
/*!40000 ALTER TABLE `defect_record` DISABLE KEYS */;
INSERT INTO `defect_record` VALUES (1,'SN-2026052801','B20260528',1,1,NULL,NULL,1,'A','2026-05-28 14:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(2,'SN-2026052802','B20260528',1,1,1,1,0,'A','2026-05-28 13:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(3,'SN-2026052803','B20260528',1,1,2,2,0,'A','2026-05-28 13:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(4,'SN-2026052804','B20260528',1,2,NULL,NULL,1,'A','2026-05-28 12:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(5,'SN-2026052805','B20260528',1,2,3,3,0,'A','2026-05-28 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(6,'SN-2026052806','B20260528',2,3,NULL,NULL,1,'A','2026-05-28 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(7,'SN-2026052807','B20260528',2,3,4,1,0,'A','2026-05-28 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(8,'SN-2026052808','B20260528',2,3,NULL,NULL,1,'A','2026-05-28 10:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(9,'SN-2026052809','B20260528',3,4,1,2,0,'B','2026-05-28 10:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(10,'SN-2026052810','B20260528',3,4,NULL,NULL,1,'B','2026-05-28 09:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(11,'SN-2026052811','B20260528',3,4,5,1,0,'B','2026-05-28 09:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(12,'SN-2026052812','B20260528',4,5,NULL,NULL,1,'B','2026-05-28 08:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(13,'SN-2026052813','B20260528',4,5,NULL,NULL,1,'B','2026-05-28 08:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(14,'SN-2026052814','B20260528',4,5,6,2,0,'B','2026-05-28 07:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(15,'SN-2026052815','B20260528',4,5,NULL,NULL,1,'B','2026-05-28 07:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(16,'SN-2026052701','B20260527',1,1,NULL,NULL,1,'A','2026-05-27 13:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(17,'SN-2026052702','B20260527',1,1,1,1,0,'A','2026-05-27 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(18,'SN-2026052703','B20260527',1,1,2,2,0,'A','2026-05-27 11:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(19,'SN-2026052704','B20260527',2,3,NULL,NULL,1,'A','2026-05-27 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(20,'SN-2026052705','B20260527',2,3,3,3,0,'A','2026-05-27 10:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(21,'SN-2026052706','B20260527',2,3,NULL,NULL,1,'B','2026-05-27 10:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(22,'SN-2026052707','B20260527',3,4,4,1,0,'B','2026-05-27 09:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(23,'SN-2026052708','B20260527',3,4,NULL,NULL,1,'B','2026-05-27 08:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(24,'SN-2026052709','B20260527',4,5,NULL,NULL,1,'C','2026-05-27 08:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(25,'SN-2026052710','B20260527',4,5,5,2,0,'C','2026-05-27 07:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(26,'SN-2026052601','B20260526',1,1,NULL,NULL,1,'A','2026-05-26 13:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(27,'SN-2026052602','B20260526',1,1,1,2,0,'A','2026-05-26 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(28,'SN-2026052603','B20260526',2,2,2,1,0,'A','2026-05-26 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(29,'SN-2026052604','B20260526',2,2,NULL,NULL,1,'B','2026-05-26 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(30,'SN-2026052605','B20260526',3,4,3,3,0,'B','2026-05-26 11:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(31,'SN-2026052501','B20260525',1,1,NULL,NULL,1,'A','2026-05-25 13:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(32,'SN-2026052502','B20260525',1,2,1,1,0,'A','2026-05-25 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(33,'SN-2026052503','B20260525',2,3,NULL,NULL,1,'A','2026-05-25 12:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(34,'SN-2026052504','B20260525',2,3,4,2,0,'B','2026-05-25 11:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(35,'SN-2026052505','B20260525',3,4,NULL,NULL,1,'B','2026-05-25 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(36,'SN-2026052506','B20260525',4,5,5,1,0,'C','2026-05-25 10:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(37,'SN-2026052401','B20260524',1,1,NULL,NULL,1,'A','2026-05-24 13:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(38,'SN-2026052402','B20260524',1,1,2,2,0,'A','2026-05-24 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(39,'SN-2026052403','B20260524',2,3,NULL,NULL,1,'B','2026-05-24 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(40,'SN-2026052404','B20260524',3,4,3,3,0,'B','2026-05-24 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(41,'SN-2026052405','B20260524',4,5,NULL,NULL,1,'C','2026-05-24 10:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(42,'SN-2026052301','B20260523',1,1,1,1,0,'A','2026-05-23 13:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(43,'SN-2026052302','B20260523',1,2,NULL,NULL,1,'A','2026-05-23 12:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(44,'SN-2026052303','B20260523',2,3,4,2,0,'B','2026-05-23 12:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(45,'SN-2026052304','B20260523',3,4,NULL,NULL,1,'B','2026-05-23 11:59:29',0,NULL,NULL,NULL,0,'2026-05-28 15:59:29'),(46,'SN-2026052305','B20260523',4,5,6,3,0,'C','2026-05-23 10:59:29',1,NULL,NULL,NULL,0,'2026-05-28 15:59:29');
/*!40000 ALTER TABLE `defect_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `line_info`
--

DROP TABLE IF EXISTS `line_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `line_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `line_no` varchar(50) NOT NULL COMMENT '产线编号',
  `line_name` varchar(100) NOT NULL,
  `workshop` varchar(100) DEFAULT NULL COMMENT '所属车间',
  `manager_id` bigint DEFAULT NULL COMMENT '负责人ID',
  `description` varchar(255) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `line_no` (`line_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `line_info`
--

LOCK TABLES `line_info` WRITE;
/*!40000 ALTER TABLE `line_info` DISABLE KEYS */;
INSERT INTO `line_info` VALUES (1,'LINE-001','A1号产线',NULL,NULL,NULL,1,NULL,0,NULL,NULL,'2026-05-27 10:59:46','2026-05-27 10:59:46'),(2,'LINE-002','A2号产线',NULL,NULL,NULL,1,NULL,0,NULL,NULL,'2026-05-27 10:59:46','2026-05-27 10:59:46'),(3,'LINE-003','B1号产线',NULL,NULL,NULL,1,NULL,0,NULL,NULL,'2026-05-27 10:59:46','2026-05-27 10:59:46'),(4,'L001','一号产线','A车间',1,NULL,1,NULL,0,NULL,NULL,'2026-05-28 15:33:23','2026-05-28 15:33:23');
/*!40000 ALTER TABLE `line_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_type`
--

DROP TABLE IF EXISTS `product_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `line_id` bigint NOT NULL,
  `type_no` varchar(50) DEFAULT NULL COMMENT '型号编码',
  `type_name` varchar(100) NOT NULL,
  `type_code` varchar(50) DEFAULT NULL,
  `spec` varchar(255) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_type`
--

LOCK TABLES `product_type` WRITE;
/*!40000 ALTER TABLE `product_type` DISABLE KEYS */;
INSERT INTO `product_type` VALUES (1,4,'T001','A型产品',NULL,NULL,1,0,NULL,'2026-05-28 15:33:47',NULL,NULL),(2,1,'PT-002','B型产品',NULL,NULL,1,0,1,'2026-05-28 15:59:01',NULL,NULL),(3,2,'PT-003','C型产品',NULL,NULL,1,0,1,'2026-05-28 15:59:01',NULL,NULL),(4,3,'PT-004','D型产品',NULL,NULL,1,0,1,'2026-05-28 15:59:01',NULL,NULL),(5,4,'PT-005','E型产品',NULL,NULL,1,0,1,'2026-05-28 15:59:01',NULL,NULL);
/*!40000 ALTER TABLE `product_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL DEFAULT '0',
  `menu_name` varchar(80) NOT NULL,
  `menu_type` char(1) NOT NULL COMMENT 'M目录 C菜单 F按钮',
  `perm` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `path` varchar(200) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'数据统计','M',NULL,'/stats',NULL,1,1,0),(2,0,'缺陷管理','M',NULL,'/defect',NULL,2,1,0),(3,0,'产线管理','M',NULL,'/line',NULL,3,1,0),(4,0,'预警管理','M',NULL,'/alert',NULL,4,1,0),(5,0,'追溯管理','M',NULL,'/trace',NULL,5,1,0),(6,0,'系统管理','M',NULL,'/system',NULL,6,1,0),(7,1,'查看仪表盘','B','stats:dashboard:view',NULL,NULL,1,1,0),(8,1,'查看趋势','B','stats:trend:view',NULL,NULL,2,1,0),(9,2,'缺陷记录查询','B','defect:record:list',NULL,NULL,1,1,0),(10,2,'缺陷处置','B','defect:record:dispose',NULL,NULL,2,1,0),(11,2,'缺陷导出','B','defect:record:export',NULL,NULL,3,1,0),(12,2,'缺陷类型查询','B','defect:category:list',NULL,NULL,4,1,0),(13,2,'缺陷类型新增','B','defect:category:add',NULL,NULL,5,1,0),(14,2,'缺陷类型修改','B','defect:category:edit',NULL,NULL,6,1,0),(15,2,'缺陷类型删除','B','defect:category:delete',NULL,NULL,7,1,0),(16,3,'产线查询','B','line:list',NULL,NULL,1,1,0),(17,3,'产线新增','B','line:add',NULL,NULL,2,1,0),(18,3,'产线修改','B','line:edit',NULL,NULL,3,1,0),(19,3,'产线删除','B','line:delete',NULL,NULL,4,1,0),(20,3,'产品类型查询','B','product:list',NULL,NULL,5,1,0),(21,3,'产品类型新增','B','product:add',NULL,NULL,6,1,0),(22,3,'产品类型修改','B','product:edit',NULL,NULL,7,1,0),(23,3,'产品类型删除','B','product:delete',NULL,NULL,8,1,0),(24,4,'预警规则查询','B','alert:rule:list',NULL,NULL,1,1,0),(25,4,'预警规则新增','B','alert:rule:add',NULL,NULL,2,1,0),(26,4,'预警规则修改','B','alert:rule:edit',NULL,NULL,3,1,0),(27,4,'预警规则删除','B','alert:rule:delete',NULL,NULL,4,1,0),(28,4,'预警记录查询','B','alert:record:list',NULL,NULL,5,1,0),(29,4,'预警处理','B','alert:record:handle',NULL,NULL,6,1,0),(30,5,'追溯查询','B','trace:query',NULL,NULL,1,1,0),(31,6,'用户查询','B','system:user:list',NULL,NULL,1,1,0),(32,6,'用户新增','B','system:user:add',NULL,NULL,2,1,0),(33,6,'用户修改','B','system:user:edit',NULL,NULL,3,1,0),(34,6,'用户删除','B','system:user:delete',NULL,NULL,4,1,0),(35,6,'角色查询','B','system:role:list',NULL,NULL,5,1,0),(36,6,'角色新增','B','system:role:add',NULL,NULL,6,1,0),(37,6,'角色修改','B','system:role:edit',NULL,NULL,7,1,0),(38,6,'角色删除','B','system:role:delete',NULL,NULL,8,1,0),(39,6,'菜单查询','B','system:menu:list',NULL,NULL,9,1,0),(40,6,'菜单新增','B','system:menu:add',NULL,NULL,10,1,0),(41,6,'菜单修改','B','system:menu:edit',NULL,NULL,11,1,0),(42,6,'菜单删除','B','system:menu:delete',NULL,NULL,12,1,0);
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
  `role_name` varchar(50) NOT NULL,
  `role_key` varchar(50) NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_key` (`role_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','super_admin',1,0,'2026-05-27 10:59:46'),(2,'质检员','quality_inspector',1,0,'2026-05-27 10:59:46'),(3,'产线主管','line_supervisor',1,0,'2026-05-27 10:59:46'),(4,'只读查看','viewer',1,0,'2026-05-27 10:59:46');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40),(1,41),(1,42);
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
  `username` varchar(50) NOT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `email` varchar(100) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1启用 0停用',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','超级管理员','$2a$10$r3T8bb9wR9tlqCUof.rIz.ycYcP7.sXlD8L7vDWpt4/1gZZjs8LWu',NULL,NULL,NULL,1,0,NULL,NULL,'2026-05-27 10:59:46','2026-05-28 10:54:59'),(2,'operator1','操作员一','$2a$10$CXH9bja0.fWCv33v2wHQaOznTuBuZ0GvSVFIW0TC9Zh.wQFH7tG0i','13800138001',NULL,NULL,1,0,NULL,NULL,'2026-05-28 15:34:19','2026-05-28 15:34:19');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'vision_defect_traceability_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:13:15
