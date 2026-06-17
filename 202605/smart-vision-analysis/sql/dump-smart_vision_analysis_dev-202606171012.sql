-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: smart_vision_analysis_dev
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
-- Table structure for table `image_category`
--

DROP TABLE IF EXISTS `image_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(64) NOT NULL COMMENT '分类名称',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父分类ID(0=根)',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图像分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_category`
--

LOCK TABLES `image_category` WRITE;
/*!40000 ALTER TABLE `image_category` DISABLE KEYS */;
INSERT INTO `image_category` VALUES (1,'工业零件',0,1,1,'2026-05-27 10:24:09',0),(2,'表面检测',0,2,1,'2026-05-27 10:24:09',0),(3,'医疗影像',0,3,1,'2026-05-27 10:24:09',0),(4,'焊缝检测',1,1,1,'2026-05-27 10:24:09',0),(5,'裂纹检测',1,2,1,'2026-05-27 10:24:09',0),(6,'腐蚀检测',2,1,1,'2026-05-27 10:24:09',0),(7,'测试分类',0,1,1,'2026-05-28 14:05:09',1);
/*!40000 ALTER TABLE `image_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_file`
--

DROP TABLE IF EXISTS `image_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图像ID',
  `image_no` varchar(64) NOT NULL COMMENT '图像编号',
  `file_name` varchar(255) NOT NULL COMMENT '原始文件名',
  `file_path` varchar(512) NOT NULL COMMENT '存储路径',
  `file_format` varchar(16) DEFAULT NULL COMMENT '文件格式:jpg/png/bmp',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小(Byte)',
  `category_id` bigint DEFAULT NULL COMMENT '所属分类ID',
  `recognition_status` tinyint NOT NULL DEFAULT '0' COMMENT '识别状态:0未识别 1识别中 2已完成 3识别失败',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `uploaded_by` bigint DEFAULT NULL COMMENT '上传人',
  `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_image_no` (`image_no`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_uploaded_at` (`uploaded_at`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图像文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_file`
--

LOCK TABLES `image_file` WRITE;
/*!40000 ALTER TABLE `image_file` DISABLE KEYS */;
INSERT INTO `image_file` VALUES (1,'IMG-20260101-001','weld_sample_001.jpg','/uploads/2026/01/weld_sample_001.jpg','JPEG',245678,4,2,'焊缝检测样本',1,'2026-01-05 09:10:00',0),(2,'IMG-20260101-002','weld_sample_002.jpg','/uploads/2026/01/weld_sample_002.jpg','JPEG',312456,4,2,'焊缝检测样本',1,'2026-01-05 09:15:00',0),(3,'IMG-20260101-003','crack_test_001.jpg','/uploads/2026/01/crack_test_001.jpg','JPEG',198234,5,2,'裂纹检测样本',1,'2026-01-06 10:00:00',0),(4,'IMG-20260101-004','crack_test_002.jpg','/uploads/2026/01/crack_test_002.jpg','PNG',456789,5,2,'裂纹检测样本',1,'2026-01-06 10:05:00',0),(5,'IMG-20260101-005','surface_defect_001.jpg','/uploads/2026/01/surface_defect_001.jpg','JPEG',287345,4,2,'表面缺陷样本',1,'2026-01-07 11:00:00',0),(6,'IMG-20260102-001','pcb_inspection_001.jpg','/uploads/2026/02/pcb_inspection_001.jpg','JPEG',523678,4,2,'PCB板检测样本',1,'2026-02-10 09:30:00',0),(7,'IMG-20260102-002','pcb_inspection_002.jpg','/uploads/2026/02/pcb_inspection_002.jpg','JPEG',478923,4,2,'PCB板检测样本',1,'2026-02-10 09:35:00',0),(8,'IMG-20260103-001','quality_check_001.png','/uploads/2026/03/quality_check_001.png','PNG',345678,5,0,'质量检测待识别',1,'2026-03-15 14:00:00',0),(9,'IMG-20260103-002','quality_check_002.png','/uploads/2026/03/quality_check_002.png','PNG',267891,5,0,'质量检测待识别',1,'2026-03-15 14:05:00',0),(10,'IMG-20260103-003','weld_batch_001.jpg','/uploads/2026/03/weld_batch_001.jpg','JPEG',389234,4,0,'批量焊缝检测',1,'2026-03-20 08:00:00',0),(11,'IMG-20260103-004','weld_batch_002.jpg','/uploads/2026/03/weld_batch_002.jpg','JPEG',412567,4,0,'批量焊缝检测',1,'2026-03-20 08:05:00',0),(12,'IMG-20260103-005','weld_batch_003.jpg','/uploads/2026/03/weld_batch_003.jpg','JPEG',356789,4,0,'批量焊缝检测',1,'2026-03-20 08:10:00',0);
/*!40000 ALTER TABLE `image_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_version`
--

DROP TABLE IF EXISTS `model_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `model_version` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `model_name` varchar(128) NOT NULL COMMENT '模型名称',
  `version_no` varchar(64) NOT NULL COMMENT '版本号',
  `scene_desc` varchar(255) DEFAULT NULL COMMENT '适用场景描述',
  `support_labels` text COMMENT '支持的标签(逗号分隔)',
  `release_date` date DEFAULT NULL COMMENT '发布日期',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:1可用 0废弃',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_version` (`model_name`,`version_no`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='算法模型版本表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_version`
--

LOCK TABLES `model_version` WRITE;
/*!40000 ALTER TABLE `model_version` DISABLE KEYS */;
INSERT INTO `model_version` VALUES (1,'YOLOv8-Defect','v1.0.0','通用工业缺陷检测模型','裂纹,腐蚀,气泡,划痕,脏污','2024-01-01',1,NULL,1,'2026-05-27 10:24:09','2026-05-27 10:24:09',0),(2,'ResNet50-Classify','v2.1.0','图像分类模型，适用于医疗影像辅助诊断','正常,异常,待复查','2024-03-15',1,NULL,1,'2026-05-27 10:24:09','2026-05-27 10:24:09',0),(3,'YOLOv8-Weld','v2.0.1','焊缝缺陷专用检测模型','气孔,裂纹,未熔合,夹渣','2024-06-01',1,NULL,1,'2026-05-28 11:07:07','2026-05-28 11:07:07',0),(4,'ResNet50-Quality','v1.2.0','质量检测分类模型','合格,轻微缺陷,严重缺陷','2024-09-15',1,NULL,1,'2026-05-28 11:07:07','2026-05-28 11:07:07',0),(5,'EfficientDet-PCB','v3.0.0','PCB板缺陷检测','焊点缺失,短路,划痕,污染','2025-01-20',1,NULL,1,'2026-05-28 11:07:07','2026-05-28 11:07:07',0),(6,'YOLOv8-Defect','v2.0.0','通用工业缺陷检测升级版','裂纹,腐蚀,气泡,划痕,污点','2025-03-01',1,NULL,1,'2026-05-28 11:07:08','2026-05-28 11:07:08',0);
/*!40000 ALTER TABLE `model_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recognition_box`
--

DROP TABLE IF EXISTS `recognition_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recognition_box` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '检测框ID',
  `result_id` bigint NOT NULL COMMENT '识别结果ID',
  `x` decimal(10,4) NOT NULL COMMENT '左上角X坐标(相对)',
  `y` decimal(10,4) NOT NULL COMMENT '左上角Y坐标(相对)',
  `width` decimal(10,4) NOT NULL COMMENT '宽度(相对)',
  `height` decimal(10,4) NOT NULL COMMENT '高度(相对)',
  `label` varchar(64) NOT NULL COMMENT '缺陷标签',
  `confidence` decimal(5,4) DEFAULT NULL COMMENT '置信度',
  `source` tinyint NOT NULL DEFAULT '0' COMMENT '来源:0算法 1人工',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `updated_by` bigint DEFAULT NULL COMMENT '修改人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_result_id` (`result_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='检测框表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recognition_box`
--

LOCK TABLES `recognition_box` WRITE;
/*!40000 ALTER TABLE `recognition_box` DISABLE KEYS */;
INSERT INTO `recognition_box` VALUES (1,1,0.1234,0.2345,0.3456,0.2123,'裂纹',0.9234,0,0,NULL,'2026-05-28 11:09:14'),(2,1,0.5678,0.3456,0.1234,0.1567,'气孔',0.8912,0,0,NULL,'2026-05-28 11:09:14'),(3,2,0.2345,0.3456,0.4123,0.2345,'焊缝缺陷',0.8756,0,0,NULL,'2026-05-28 11:09:14'),(4,3,0.3456,0.1234,0.2345,0.3123,'裂纹',0.9123,0,0,NULL,'2026-05-28 11:09:14'),(5,4,0.4567,0.5678,0.1234,0.1234,'污点',0.7834,0,0,NULL,'2026-05-28 11:09:14'),(6,5,0.1234,0.4567,0.2345,0.1678,'气泡',0.8567,0,0,NULL,'2026-05-28 11:09:14'),(7,5,0.6789,0.2345,0.1567,0.1456,'划痕',0.8123,0,0,NULL,'2026-05-28 11:09:14'),(8,6,0.2345,0.3456,0.3123,0.2345,'裂纹',0.9345,0,0,NULL,'2026-05-28 11:09:14'),(9,7,0.1234,0.1234,0.4567,0.3456,'深度裂纹',0.8901,0,0,NULL,'2026-05-28 11:09:14'),(10,8,0.3456,0.4567,0.2123,0.1789,'焊点缺失',0.7234,0,0,NULL,'2026-05-28 11:09:14'),(11,9,0.5678,0.3456,0.3123,0.2345,'短路风险',0.8123,0,0,NULL,'2026-05-28 11:09:14'),(12,14,0.2345,0.5678,0.1789,0.1234,'焊点缺失',0.8456,0,0,NULL,'2026-05-28 11:09:14'),(13,17,0.4567,0.3456,0.2345,0.1678,'污染',0.8234,0,0,NULL,'2026-05-28 11:09:14'),(14,18,0.1234,0.2345,0.3456,0.2123,'短路',0.7645,0,0,NULL,'2026-05-28 11:09:14'),(15,18,0.5678,0.4567,0.2123,0.1456,'污染',0.7812,0,0,NULL,'2026-05-28 11:09:14'),(16,19,0.3456,0.2345,0.1234,0.1567,'合格',0.9234,0,0,NULL,'2026-05-28 11:09:14'),(17,26,0.2345,0.3456,0.3123,0.2345,'裂纹',0.8234,0,0,NULL,'2026-05-28 11:09:14'),(18,27,0.4567,0.1234,0.2345,0.3456,'深度裂纹',0.7912,0,0,NULL,'2026-05-28 11:09:14');
/*!40000 ALTER TABLE `recognition_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recognition_result`
--

DROP TABLE IF EXISTS `recognition_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recognition_result` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '结果ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `image_id` bigint NOT NULL COMMENT '图像ID',
  `review_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态:0待审核 1已确认 2需修正 3已修正',
  `reviewed_by` bigint DEFAULT NULL COMMENT '审核人',
  `reviewed_at` datetime DEFAULT NULL COMMENT '审核时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_image_id` (`image_id`),
  KEY `idx_review_status` (`review_status`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='识别结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recognition_result`
--

LOCK TABLES `recognition_result` WRITE;
/*!40000 ALTER TABLE `recognition_result` DISABLE KEYS */;
INSERT INTO `recognition_result` VALUES (1,1,1,2,1,'2026-01-05 10:00:00','2026-01-05 09:35:00','2026-05-28 11:09:14'),(2,1,2,2,1,'2026-01-05 10:05:00','2026-01-05 09:36:00','2026-05-28 11:09:14'),(3,1,3,2,1,'2026-01-05 10:08:00','2026-01-05 09:38:00','2026-05-28 11:09:14'),(4,1,4,1,NULL,NULL,'2026-01-05 09:40:00','2026-05-28 11:09:14'),(5,1,5,2,1,'2026-01-05 10:15:00','2026-01-05 09:42:00','2026-05-28 11:09:14'),(6,2,3,2,1,'2026-01-06 11:00:00','2026-01-06 10:32:00','2026-05-28 11:09:14'),(7,2,4,2,1,'2026-01-06 11:05:00','2026-01-06 10:34:00','2026-05-28 11:09:14'),(8,3,6,2,1,'2026-02-10 11:00:00','2026-02-10 10:02:00','2026-05-28 11:09:14'),(9,3,7,3,1,'2026-02-10 11:10:00','2026-02-10 10:05:00','2026-05-28 11:09:14'),(10,7,1,2,1,'2026-04-15 15:00:00','2026-04-15 14:03:00','2026-05-28 11:09:14'),(11,7,2,2,1,'2026-04-15 15:05:00','2026-04-15 14:06:00','2026-05-28 11:09:14'),(12,7,5,2,1,'2026-04-15 15:08:00','2026-04-15 14:09:00','2026-05-28 11:09:14'),(13,7,6,2,1,'2026-04-15 15:10:00','2026-04-15 14:12:00','2026-05-28 11:09:14'),(14,8,1,2,1,'2026-05-01 10:00:00','2026-05-01 09:05:00','2026-05-28 11:09:14'),(15,8,2,2,1,'2026-05-01 10:05:00','2026-05-01 09:10:00','2026-05-28 11:09:14'),(16,8,6,2,1,'2026-05-01 10:08:00','2026-05-01 09:15:00','2026-05-28 11:09:14'),(17,8,7,3,1,'2026-05-01 10:12:00','2026-05-01 09:20:00','2026-05-28 11:09:14'),(18,8,3,2,1,'2026-05-01 10:15:00','2026-05-01 09:25:00','2026-05-28 11:09:14'),(19,8,5,1,NULL,NULL,'2026-05-01 09:30:00','2026-05-28 11:09:14'),(20,9,1,2,1,'2026-05-15 14:00:00','2026-05-15 13:05:00','2026-05-28 11:09:14'),(21,9,2,2,1,'2026-05-15 14:05:00','2026-05-15 13:08:00','2026-05-28 11:09:14'),(22,9,3,2,1,'2026-05-15 14:08:00','2026-05-15 13:12:00','2026-05-28 11:09:14'),(23,9,6,2,1,'2026-05-15 14:10:00','2026-05-15 13:16:00','2026-05-28 11:09:14'),(24,9,7,2,1,'2026-05-15 14:12:00','2026-05-15 13:20:00','2026-05-28 11:09:14'),(25,10,3,2,1,'2026-05-20 11:00:00','2026-05-20 10:05:00','2026-05-28 11:09:14'),(26,10,4,3,1,'2026-05-20 11:05:00','2026-05-20 10:08:00','2026-05-28 11:09:14'),(27,10,5,2,1,'2026-05-20 11:08:00','2026-05-20 10:12:00','2026-05-28 11:09:14');
/*!40000 ALTER TABLE `recognition_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recognition_task`
--

DROP TABLE IF EXISTS `recognition_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recognition_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_no` varchar(64) NOT NULL COMMENT '任务编号',
  `task_name` varchar(128) NOT NULL COMMENT '任务名称',
  `model_version_id` bigint NOT NULL COMMENT '模型版本ID',
  `model_version_no` varchar(64) DEFAULT NULL COMMENT '模型版本号(冗余)',
  `confidence_threshold` decimal(5,4) NOT NULL DEFAULT '0.5000' COMMENT '置信度阈值',
  `select_mode` tinyint NOT NULL DEFAULT '0' COMMENT '选图模式:0按分类 1手动选择',
  `total_count` int NOT NULL DEFAULT '0' COMMENT '总图像数',
  `processed_count` int NOT NULL DEFAULT '0' COMMENT '已处理数',
  `success_count` int NOT NULL DEFAULT '0' COMMENT '成功数',
  `fail_count` int NOT NULL DEFAULT '0' COMMENT '失败数',
  `avg_confidence` decimal(5,4) DEFAULT NULL COMMENT '平均置信度',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '任务状态:0待执行 1执行中 2已完成 3已失败 4已取消',
  `fail_reason` text COMMENT '失败原因',
  `started_at` datetime DEFAULT NULL COMMENT '开始时间',
  `finished_at` datetime DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_no` (`task_no`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='识别任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recognition_task`
--

LOCK TABLES `recognition_task` WRITE;
/*!40000 ALTER TABLE `recognition_task` DISABLE KEYS */;
INSERT INTO `recognition_task` VALUES (1,'TASK-20260105-001','焊缝缺陷批量检测-第一批',1,'v1.0.0',0.7500,1,5,5,4,1,0.8821,3,NULL,'2026-01-05 09:30:00','2026-01-05 09:45:00','焊缝样本第一批次检测',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(2,'TASK-20260106-001','裂纹检测专项任务',1,'v1.0.0',0.8000,1,2,2,2,0,0.9123,3,NULL,'2026-01-06 10:30:00','2026-01-06 10:38:00','裂纹样本检测',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(3,'TASK-20260210-001','PCB板质量检测',3,'v3.0.0',0.8500,1,2,2,1,1,0.7645,3,NULL,'2026-02-10 10:00:00','2026-02-10 10:12:00','PCB板缺陷检测任务',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(4,'TASK-20260315-001','质量分类检测-3月批',2,'v1.2.0',0.7000,1,2,0,0,0,NULL,1,NULL,NULL,NULL,'待执行质量检测任务',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(5,'TASK-20260320-001','焊缝批量检测-3月',1,'v1.0.0',0.7500,1,3,0,0,0,NULL,0,NULL,NULL,NULL,'已提交待处理',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(6,'TASK-20260410-001','综合缺陷检测-4月',5,'v2.0.0',0.8000,1,7,3,2,1,0.8234,2,NULL,'2026-04-10 08:00:00',NULL,'识别进行中',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(7,'TASK-20260415-001','焊接质量巡检',1,'v1.0.0',0.7500,1,4,4,4,0,0.9345,3,NULL,'2026-04-15 14:00:00','2026-04-15 14:18:00','焊接巡检合格批次',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(8,'TASK-20260501-001','五月份PCB全量检测',3,'v3.0.0',0.8500,1,6,6,5,1,0.8567,3,NULL,'2026-05-01 09:00:00','2026-05-01 09:35:00','五月PCB检测任务',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(9,'TASK-20260515-001','出厂前质量抽检',2,'v1.2.0',0.8000,1,5,5,5,0,0.9678,3,NULL,'2026-05-15 13:00:00','2026-05-15 13:28:00','出厂抽检全部合格',1,'2026-05-28 11:08:03','2026-05-28 11:08:03'),(10,'TASK-20260520-001','缺陷复检任务',5,'v2.0.0',0.9000,1,3,3,2,1,0.8123,3,NULL,'2026-05-20 10:00:00','2026-05-20 10:15:00','对上次失败项目复检',1,'2026-05-28 11:08:03','2026-05-28 11:08:03');
/*!40000 ALTER TABLE `recognition_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_task_summary`
--

DROP TABLE IF EXISTS `report_task_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_task_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `total_images` int NOT NULL DEFAULT '0' COMMENT '总图像数',
  `success_count` int NOT NULL DEFAULT '0' COMMENT '识别成功数',
  `fail_count` int NOT NULL DEFAULT '0' COMMENT '识别失败数',
  `avg_confidence` decimal(5,4) DEFAULT NULL COMMENT '平均置信度',
  `min_confidence` decimal(5,4) DEFAULT NULL COMMENT '最低置信度',
  `max_confidence` decimal(5,4) DEFAULT NULL COMMENT '最高置信度',
  `low_confidence_count` int NOT NULL DEFAULT '0' COMMENT '低置信度数量(<0.6)',
  `category_stats` json DEFAULT NULL COMMENT '各类别统计(JSON)',
  `confidence_distribution` json DEFAULT NULL COMMENT '置信度分布(JSON)',
  `generated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_id` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报告任务汇总表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_task_summary`
--

LOCK TABLES `report_task_summary` WRITE;
/*!40000 ALTER TABLE `report_task_summary` DISABLE KEYS */;
INSERT INTO `report_task_summary` VALUES (1,1,8,7,1,0.8245,0.6120,0.9870,2,'{\"气泡\": 3, \"腐蚀\": 5, \"裂纹\": 12}','{\"0.6-0.7\": 1, \"0.7-0.8\": 2, \"0.8-0.9\": 8, \"0.9-1.0\": 4}','2026-05-15 16:30:00'),(2,2,5,5,0,0.8890,0.7450,0.9650,1,'{\"腐蚀\": 2, \"裂纹\": 8}','{\"0.7-0.8\": 1, \"0.8-0.9\": 4, \"0.9-1.0\": 5}','2026-05-16 10:20:00'),(3,3,6,6,0,0.9120,0.8210,0.9780,0,'{\"合格\": 20, \"严重缺陷\": 2, \"轻微缺陷\": 6}','{\"0.8-0.9\": 3, \"0.9-1.0\": 6}','2026-05-17 14:00:00'),(4,4,7,6,1,0.8560,0.6890,0.9540,1,'{\"夹渣\": 2, \"气孔\": 4, \"裂纹\": 6}','{\"0.6-0.7\": 1, \"0.7-0.8\": 2, \"0.8-0.9\": 5, \"0.9-1.0\": 4}','2026-05-18 09:45:00'),(5,5,8,7,1,0.8730,0.7120,0.9620,2,'{\"划痕\": 9, \"气泡\": 3, \"污点\": 4}','{\"0.7-0.8\": 2, \"0.8-0.9\": 4, \"0.9-1.0\": 7}','2026-05-19 11:30:00'),(6,6,9,8,1,0.8940,0.7560,0.9880,1,'{\"划痕\": 3, \"气泡\": 2, \"腐蚀\": 4, \"裂纹\": 7}','{\"0.7-0.8\": 1, \"0.8-0.9\": 5, \"0.9-1.0\": 9}','2026-05-20 15:20:00'),(7,7,6,6,0,0.9210,0.8340,0.9760,0,'{\"气孔\": 4, \"未熔合\": 2, \"焊缝缺陷\": 8}','{\"0.8-0.9\": 2, \"0.9-1.0\": 6}','2026-05-21 08:50:00'),(8,8,7,7,0,0.9050,0.8120,0.9820,0,'{\"合格\": 22, \"严重缺陷\": 1, \"轻微缺陷\": 5}','{\"0.8-0.9\": 4, \"0.9-1.0\": 7}','2026-05-22 16:10:00'),(9,9,5,4,1,0.8340,0.6780,0.9450,1,'{\"污点\": 2, \"腐蚀\": 3, \"裂纹\": 5}','{\"0.6-0.7\": 1, \"0.7-0.8\": 1, \"0.8-0.9\": 3, \"0.9-1.0\": 4}','2026-05-23 10:30:00'),(10,10,8,7,1,0.8680,0.7120,0.9580,2,'{\"划痕\": 3, \"气泡\": 4, \"腐蚀\": 2, \"裂纹\": 6}','{\"0.7-0.8\": 2, \"0.8-0.9\": 4, \"0.9-1.0\": 7}','2026-05-24 14:45:00');
/*!40000 ALTER TABLE `report_task_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父菜单ID(0=根)',
  `menu_name` varchar(64) NOT NULL COMMENT '菜单名称',
  `menu_type` char(1) NOT NULL DEFAULT 'M' COMMENT '类型:M目录 C菜单 F按钮',
  `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(128) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `visible` tinyint NOT NULL DEFAULT '1' COMMENT '是否显示:1显示 0隐藏',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:1启用 0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'影像管理','M','/image',NULL,NULL,'picture',1,1,1),(2,0,'识别任务','M','/task',NULL,NULL,'play-circle',2,1,1),(3,0,'识别结果','M','/result',NULL,NULL,'eye',3,1,1),(4,0,'分析报告','M','/report',NULL,NULL,'bar-chart',4,1,1),(5,0,'模型版本','C','/model',NULL,NULL,'robot',5,1,1),(6,0,'系统管理','M','/system',NULL,NULL,'setting',6,1,1),(10,1,'影像列表','C','/image/list',NULL,'image:file:list','file-image',1,1,1),(11,1,'影像上传','F',NULL,NULL,'image:file:upload',NULL,2,1,1),(12,1,'影像删除','F',NULL,NULL,'image:file:delete',NULL,3,1,1),(13,1,'分类管理','C','/image/category',NULL,'image:category:list','folder',4,1,1),(14,1,'分类新增','F',NULL,NULL,'image:category:add',NULL,5,1,1),(15,1,'分类修改','F',NULL,NULL,'image:category:edit',NULL,6,1,1),(16,1,'分类删除','F',NULL,NULL,'image:category:delete',NULL,7,1,1),(20,2,'任务列表','C','/task/list',NULL,'task:list','unordered-list',1,1,1),(21,2,'任务新增','F',NULL,NULL,'task:create',NULL,2,1,1),(22,2,'任务删除','F',NULL,NULL,'task:delete',NULL,3,1,1),(30,3,'结果列表','C','/result/list',NULL,'result:list','unordered-list',1,1,1),(31,3,'结果审核','F',NULL,NULL,'result:review',NULL,2,1,1),(40,4,'任务报告','C','/report/task',NULL,'report:view','bar-chart',1,1,1),(41,4,'汇总报告','C','/report/summary',NULL,'report:view','line-chart',2,1,1),(50,5,'模型列表','F',NULL,NULL,'model:list',NULL,1,1,1),(51,5,'模型新增','F',NULL,NULL,'model:add',NULL,2,1,1),(52,5,'模型修改','F',NULL,NULL,'model:edit',NULL,3,1,1),(53,5,'模型删除','F',NULL,NULL,'model:delete',NULL,4,1,1),(60,6,'用户管理','C','/system/user',NULL,'system:user:list','user',1,1,1),(61,6,'用户新增','F',NULL,NULL,'system:user:add',NULL,2,1,1),(62,6,'用户修改','F',NULL,NULL,'system:user:edit',NULL,3,1,1),(63,6,'用户删除','F',NULL,NULL,'system:user:delete',NULL,4,1,1),(64,6,'角色管理','C','/system/role',NULL,'system:role:list','team',2,1,1),(65,6,'角色新增','F',NULL,NULL,'system:role:add',NULL,3,1,1),(66,6,'角色修改','F',NULL,NULL,'system:role:edit',NULL,4,1,1),(67,6,'角色删除','F',NULL,NULL,'system:role:delete',NULL,5,1,1),(68,6,'菜单管理','C','/system/menu',NULL,'system:menu:list','menu',3,1,1),(69,6,'菜单新增','F',NULL,NULL,'system:menu:add',NULL,4,1,1),(70,6,'菜单修改','F',NULL,NULL,'system:menu:edit',NULL,5,1,1),(71,6,'菜单删除','F',NULL,NULL,'system:menu:delete',NULL,6,1,1),(72,6,'操作日志','C','/system/log',NULL,'system:log:list','file-text',4,1,1),(73,6,'日志清除','F',NULL,NULL,'system:log:delete',NULL,5,1,1);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operation_log`
--

DROP TABLE IF EXISTS `sys_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `module` varchar(64) DEFAULT NULL COMMENT '模块名',
  `operation` varchar(128) DEFAULT NULL COMMENT '操作描述',
  `request_method` varchar(16) DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(512) DEFAULT NULL COMMENT '请求地址',
  `request_params` text COMMENT '请求参数',
  `response_result` text COMMENT '返回结果',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:1成功 0失败',
  `ip` varchar(64) DEFAULT NULL COMMENT '操作IP',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operation_log`
--

LOCK TABLES `sys_operation_log` WRITE;
/*!40000 ALTER TABLE `sys_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:1启用 0禁用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','SUPER_ADMIN','系统最高权限角色',1,'2026-05-27 10:24:09'),(2,'算法工程师','ALGO_ENG','负责模型版本管理和任务管理',1,'2026-05-27 10:24:09'),(3,'图像分析员','IMG_ANALYST','负责图像上传和结果审核',1,'2026-05-27 10:24:09'),(4,'只读用户','READONLY','仅查看权限',1,'2026-05-27 10:24:09');
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
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,20),(1,21),(1,22),(1,30),(1,31),(1,40),(1,41),(1,50),(1,51),(1,52),(1,53),(1,60),(1,61),(1,62),(1,63),(1,64),(1,65),(1,66),(1,67),(1,68),(1,69),(1,70),(1,71),(1,72),(1,73);
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
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码(BCrypt)',
  `real_name` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `department` varchar(64) DEFAULT NULL COMMENT '部门',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:1启用 0禁用',
  `last_login_at` datetime DEFAULT NULL COMMENT '最后登录时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除:0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$mn16M7CTdePcWkmiZeNoTubg/fEswUXPJeeWeVFuxRhljHzr7da16','系统管理员',NULL,'技术部',1,NULL,NULL,'2026-05-27 10:24:09','2026-05-28 10:48:23',0);
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
-- Table structure for table `task_image_rel`
--

DROP TABLE IF EXISTS `task_image_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_image_rel` (
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `image_id` bigint NOT NULL COMMENT '图像ID',
  `result_status` tinyint NOT NULL DEFAULT '0' COMMENT '处理状态:0待处理 1成功 2失败',
  PRIMARY KEY (`task_id`,`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务图像关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_image_rel`
--

LOCK TABLES `task_image_rel` WRITE;
/*!40000 ALTER TABLE `task_image_rel` DISABLE KEYS */;
INSERT INTO `task_image_rel` VALUES (1,1,0),(1,2,0),(1,3,0),(1,4,0),(1,5,0),(2,3,0),(2,4,0),(3,6,0),(3,7,0),(7,1,0),(7,2,0),(7,5,0),(7,6,0),(8,1,0),(8,2,0),(8,3,0),(8,5,0),(8,6,0),(8,7,0),(9,1,0),(9,2,0),(9,3,0),(9,6,0),(9,7,0),(10,3,0),(10,4,0),(10,5,0);
/*!40000 ALTER TABLE `task_image_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'smart_vision_analysis_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:12:17
