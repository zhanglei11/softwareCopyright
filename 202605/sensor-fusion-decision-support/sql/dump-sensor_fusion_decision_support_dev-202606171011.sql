-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: sensor_fusion_decision_support_dev
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
  `id` bigint NOT NULL,
  `ds_code` varchar(32) NOT NULL COMMENT '数据源编号（系统生成）',
  `ds_name` varchar(100) NOT NULL COMMENT '数据源名称',
  `scene_type` varchar(20) NOT NULL COMMENT 'QUALITY/STORAGE/LOGISTICS/SECURITY',
  `ds_type` varchar(30) NOT NULL COMMENT 'DEVICE/FILE_SERVER/DATABASE/OBJECT_STORAGE',
  `conn_host` varchar(200) NOT NULL COMMENT '连接地址',
  `conn_port` int NOT NULL COMMENT '端口',
  `auth_type` varchar(20) NOT NULL DEFAULT 'NONE' COMMENT 'NONE/PASSWORD/KEY',
  `auth_config` text COMMENT '认证配置（AES-256加密JSON）',
  `field_mapping` text COMMENT '字段映射配置（JSON）',
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ds_code` (`ds_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datasource_config`
--

LOCK TABLES `datasource_config` WRITE;
/*!40000 ALTER TABLE `datasource_config` DISABLE KEYS */;
INSERT INTO `datasource_config` VALUES (2059945617949593600,'DS_VIS_CAM_01','1号可见光摄像机','URBAN_ROAD','HTTP_PUSH','192.168.1.101',8080,'BASIC','{\"username\":\"cam01\",\"password\":\"cam01@pwd\"}','{\"timestamp\":\"ts\",\"imageUrl\":\"url\",\"resolution\":\"res\"}',1,0,'2026-05-28 18:31:21',1,'2026-05-28 18:32:16',1,'城市道路1号可见光传感器'),(2059945618767482880,'DS_IR_CAM_01','1号红外热像仪','URBAN_ROAD','MQTT','192.168.1.102',1883,'TOKEN','{\"token\":\"ir-sensor-token-abc123\"}','{\"timestamp\":\"ts\",\"heatmap\":\"hm\",\"minTemp\":\"tMin\",\"maxTemp\":\"tMax\"}',1,0,'2026-05-28 18:31:22',1,'2026-05-28 18:32:16',1,'城市道路1号红外传感器'),(2059945619304353792,'DS_RADAR_01','1号毫米波雷达','HIGHWAY','TCP_STREAM','192.168.1.103',9090,'NONE','{}','{\"timestamp\":\"ts\",\"targets\":\"tgt\",\"range\":\"r\",\"velocity\":\"v\"}',1,0,'2026-05-28 18:31:22',1,'2026-05-28 18:32:16',1,'高速公路1号毫米波雷达'),(2059945619878973440,'DS_LIDAR_01','1号LiDAR点云传感器','HIGHWAY','HTTP_PULL','192.168.1.104',8081,'BASIC','{\"username\":\"lidar01\",\"password\":\"lidar@2026\"}','{\"timestamp\":\"ts\",\"pointCloud\":\"pc\",\"objectCount\":\"cnt\"}',1,0,'2026-05-28 18:31:22',1,'2026-05-28 18:32:16',1,'高速公路1号激光雷达'),(2059945620457787392,'DS_WEATHER_01','气象环境传感器','AIRPORT','MQTT','192.168.1.105',1883,'TOKEN','{\"token\":\"weather-token-xyz789\"}','{\"timestamp\":\"ts\",\"temp\":\"t\",\"humidity\":\"h\",\"windSpeed\":\"ws\",\"visibility\":\"vis\"}',1,0,'2026-05-28 18:31:22',1,'2026-05-28 18:32:16',1,'机场气象环境传感器（当前离线）'),(2059947826951098368,'DS_TEST_TMP','已编辑临时数据源','HIGHWAY','MQTT','127.0.0.1',1883,'NONE','{}','{}',1,1,'2026-05-28 18:40:08',1,'2026-05-28 18:46:06',1,NULL),(2059948933173612544,'DS_DEL_TEST','删除测试','URBAN_ROAD','HTTP_PUSH','127.0.0.1',9999,'NONE','{}','{}',1,1,'2026-05-28 18:44:32',1,'2026-05-28 18:46:06',NULL,NULL),(2059949299894194176,'DS_DEL_TEST2','删除测试2','URBAN_ROAD','HTTP_PUSH','127.0.0.1',9999,'NONE','{}','{}',1,1,'2026-05-28 18:45:59',1,'2026-05-28 18:46:00',1,NULL);
/*!40000 ALTER TABLE `datasource_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datasource_status`
--

DROP TABLE IF EXISTS `datasource_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datasource_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ds_id` bigint NOT NULL COMMENT '数据源ID',
  `conn_status` tinyint NOT NULL DEFAULT '1' COMMENT '0-异常 1-正常',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '异常描述',
  `last_data_time` datetime DEFAULT NULL COMMENT '最近一次数据更新时间',
  `checked_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
  PRIMARY KEY (`id`),
  KEY `idx_ds_id` (`ds_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源状态记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datasource_status`
--

LOCK TABLES `datasource_status` WRITE;
/*!40000 ALTER TABLE `datasource_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `datasource_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `decision_condition`
--

DROP TABLE IF EXISTS `decision_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `decision_condition` (
  `id` bigint NOT NULL,
  `condition_name` varchar(100) NOT NULL COMMENT '条件名称',
  `condition_field` varchar(100) NOT NULL COMMENT '作用字段',
  `operator` varchar(20) NOT NULL COMMENT 'GT/LT/EQ/BETWEEN/CONTAINS',
  `threshold_value` varchar(200) NOT NULL COMMENT '阈值',
  `threshold_value2` varchar(200) DEFAULT NULL COMMENT '阈值2（BETWEEN时使用）',
  `description` varchar(300) DEFAULT NULL COMMENT '条件用途说明',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='决策条件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `decision_condition`
--

LOCK TABLES `decision_condition` WRITE;
/*!40000 ALTER TABLE `decision_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `decision_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `decision_result`
--

DROP TABLE IF EXISTS `decision_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `decision_result` (
  `id` bigint NOT NULL,
  `result_code` varchar(32) NOT NULL COMMENT '结果编号（系统生成）',
  `fusion_result_id` bigint NOT NULL COMMENT '关联融合结果ID',
  `rule_id` bigint NOT NULL COMMENT '触发的决策规则ID',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称（冗余）',
  `scheme_id` bigint DEFAULT NULL COMMENT '融合方案ID（冗余）',
  `scheme_name` varchar(100) DEFAULT NULL COMMENT '融合方案名称（冗余）',
  `decision_output` text NOT NULL COMMENT '决策输出内容',
  `triggered_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '触发时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_result_code` (`result_code`),
  KEY `idx_fusion_result_id` (`fusion_result_id`),
  KEY `idx_rule_id` (`rule_id`),
  KEY `idx_triggered_at` (`triggered_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='决策结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `decision_result`
--

LOCK TABLES `decision_result` WRITE;
/*!40000 ALTER TABLE `decision_result` DISABLE KEYS */;
INSERT INTO `decision_result` VALUES (2059951001000000001,'DR-20260528-001',2059950001000000003,2059946329815257088,'车辆超速告警',2059946055377752064,'高速公路雷达-激光雷达融合方案','{\"action\":\"ALERT\",\"level\":\"HIGH\",\"message\":\"车辆超速，速度132km/h超过120km/h限速\",\"vehicleId\":2,\"actualSpeed\":132}','2026-05-28 10:05:46'),(2059951001000000002,'DR-20260528-002',2059950001000000006,2059946329815257088,'车辆超速告警',2059946055377752064,'高速公路雷达-激光雷达融合方案','{\"action\":\"ALERT\",\"level\":\"HIGH\",\"message\":\"车辆严重超速，速度145km/h超过120km/h限速\",\"vehicleId\":1,\"actualSpeed\":145}','2026-05-28 14:10:57'),(2059951001000000003,'DR-20260528-003',2059950001000000008,2059946330368905216,'道路拥堵检测',2059946055377752064,'高速公路雷达-激光雷达融合方案','{\"action\":\"ALERT\",\"level\":\"MEDIUM\",\"message\":\"道路拥堵，平均速度16km/h，11辆车低速行驶\",\"avgSpeed\":16,\"vehicleCount\":11}','2026-05-28 17:55:44'),(2059951001000000004,'DR-20260528-004',2059950001000000007,2059946330863833088,'夜间模式自动切换',2059946054681497600,'城市道路可见光-红外融合方案','{\"action\":\"SWITCH_MODE\",\"level\":\"LOW\",\"message\":\"自动切换夜间红外增强模式，当前光照强度42lux\",\"luminance\":42,\"hour\":20}','2026-05-28 20:33:28'),(2059951001000000005,'DR-20260528-005',2059950001000000005,2059946331362955264,'异常目标检测告警',2059946054681497600,'城市道路可见光-红外融合方案','{\"action\":\"RECORD\",\"level\":\"HIGH\",\"message\":\"发现异常高温目标，置信度0.71，红外温差18.3°C\",\"confidence\":0.71,\"heatDiff\":18.3}','2026-05-28 12:45:11'),(2059951001000000006,'DR-20260527-001',2059950001000000004,2059946329815257088,'车辆超速告警',2059946055377752064,'高速公路雷达-激光雷达融合方案','{\"action\":\"ALERT\",\"level\":\"HIGH\",\"message\":\"车辆超速，速度118km/h超过120km/h限速\",\"vehicleId\":1,\"actualSpeed\":118}','2026-05-28 11:20:33');
/*!40000 ALTER TABLE `decision_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `decision_rule`
--

DROP TABLE IF EXISTS `decision_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `decision_rule` (
  `id` bigint NOT NULL,
  `rule_code` varchar(32) NOT NULL COMMENT '规则编号（系统生成）',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `scheme_id` bigint NOT NULL COMMENT '关联融合方案ID',
  `trigger_condition` text COMMENT '触发条件表达式（JSON）',
  `decision_output` text NOT NULL COMMENT '决策输出内容',
  `priority` int NOT NULL DEFAULT '100' COMMENT '优先级（数值越小越高）',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0-停用 1-启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`),
  KEY `idx_scheme_id` (`scheme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='决策规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `decision_rule`
--

LOCK TABLES `decision_rule` WRITE;
/*!40000 ALTER TABLE `decision_rule` DISABLE KEYS */;
INSERT INTO `decision_rule` VALUES (2059946329815257088,'DR_SPEED_ALERT','车辆超速告警',2059946055377752064,'fusedVelocity > 120','{\"action\":\"ALERT\",\"level\":\"HIGH\",\"message\":\"车辆超速，速度超过120km/h\"}',1,1,0,'2026-05-28 18:34:11',1,'2026-05-28 18:34:11',NULL,NULL),(2059946330368905216,'DR_CONGESTION','道路拥堵检测',2059946055377752064,'avgVelocity < 20 AND objectCount > 10','{\"action\":\"ALERT\",\"level\":\"MEDIUM\",\"message\":\"道路拥堵，建议疏导\"}',2,1,0,'2026-05-28 18:34:11',1,'2026-05-28 18:34:11',NULL,NULL),(2059946330863833088,'DR_NIGHT_ENHANCE','夜间模式自动切换',2059946054681497600,'luminance < 50 AND hour >= 20','{\"action\":\"SWITCH_MODE\",\"level\":\"LOW\",\"message\":\"自动切换夜间红外增强模式\"}',3,1,0,'2026-05-28 18:34:11',1,'2026-05-28 18:34:11',NULL,NULL),(2059946331362955264,'DR_ANOMALY_DETECT','异常目标检测告警',2059946054681497600,'targetConfidence < 0.3 AND heatDiff > 15','{\"action\":\"RECORD\",\"level\":\"HIGH\",\"message\":\"发现异常高温目标，已触发记录\"}',1,1,0,'2026-05-28 18:34:12',1,'2026-05-28 18:34:12',NULL,NULL),(2059946331870466048,'DR_IDLE_SHUTDOWN','空闲自动休眠',2059946054681497600,'objectCount == 0 AND duration > 300','{\"action\":\"STANDBY\",\"level\":\"LOW\",\"message\":\"无目标超过5分钟，进入低功耗待机\"}',5,0,0,'2026-05-28 18:34:12',1,'2026-05-28 18:34:12',NULL,NULL),(2059947831313174528,'DR_TEST_TMP','已编辑临时规则',2059946054681497600,'x > 1','{\"action\":\"TEST2\"}',8,0,1,'2026-05-28 18:40:09',1,'2026-05-28 18:40:10',1,NULL);
/*!40000 ALTER TABLE `decision_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fusion_result`
--

DROP TABLE IF EXISTS `fusion_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fusion_result` (
  `id` bigint NOT NULL,
  `result_code` varchar(32) NOT NULL COMMENT '结果编号（系统生成）',
  `scheme_id` bigint NOT NULL COMMENT '融合方案ID',
  `scheme_name` varchar(100) DEFAULT NULL COMMENT '融合方案名称（冗余）',
  `scene_count` int NOT NULL DEFAULT '0' COMMENT '参与场景数',
  `data_record_count` int NOT NULL DEFAULT '0' COMMENT '融合数据条数',
  `result_status` tinyint NOT NULL DEFAULT '1' COMMENT '0-异常 1-成功',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '异常描述',
  `raw_data_summary` json DEFAULT NULL COMMENT '各场景原始数据摘要（JSON）',
  `fusion_data` json DEFAULT NULL COMMENT '融合后综合数据字段（JSON）',
  `executed_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '融合执行时间',
  `created_by` bigint DEFAULT NULL COMMENT '执行人/系统',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_result_code` (`result_code`),
  KEY `idx_scheme_id` (`scheme_id`),
  KEY `idx_executed_at` (`executed_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='融合结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fusion_result`
--

LOCK TABLES `fusion_result` WRITE;
/*!40000 ALTER TABLE `fusion_result` DISABLE KEYS */;
INSERT INTO `fusion_result` VALUES (2059950001000000001,'FR-20260528-001',2059946054681497600,'城市道路可见光-红外融合方案',3,1520,1,NULL,'{\"sources\": [\"DS_VIS_CAM_01\", \"DS_IR_CAM_01\"], \"duration\": \"2.3s\"}','{\"objects\": [{\"id\": 1, \"bbox\": [120, 80, 340, 220], \"type\": \"vehicle\", \"confidence\": 0.92}, {\"id\": 2, \"bbox\": [450, 90, 510, 280], \"type\": \"pedestrian\", \"confidence\": 0.87}], \"infraredEnhanced\": true}','2026-05-28 08:15:03',1),(2059950001000000002,'FR-20260528-002',2059946054681497600,'城市道路可见光-红外融合方案',3,1680,1,NULL,'{\"sources\": [\"DS_VIS_CAM_01\", \"DS_IR_CAM_01\"], \"duration\": \"1.9s\"}','{\"objects\": [{\"id\": 1, \"bbox\": [100, 75, 360, 230], \"type\": \"vehicle\", \"confidence\": 0.95}, {\"id\": 2, \"bbox\": [520, 85, 680, 240], \"type\": \"vehicle\", \"confidence\": 0.89}], \"infraredEnhanced\": true}','2026-05-28 09:30:17',1),(2059950001000000003,'FR-20260528-003',2059946055377752064,'高速公路雷达-激光雷达融合方案',5,3200,1,NULL,'{\"sources\": [\"DS_RADAR_01\", \"DS_LIDAR_01\"], \"duration\": \"3.1s\"}','{\"objects\": [{\"id\": 1, \"type\": \"truck\", \"range\": 250.5, \"velocity\": 105}, {\"id\": 2, \"type\": \"car\", \"range\": 180.2, \"velocity\": 132}], \"lidarPointCount\": 48500}','2026-05-28 10:05:44',1),(2059950001000000004,'FR-20260528-004',2059946055377752064,'高速公路雷达-激光雷达融合方案',5,2980,1,NULL,'{\"sources\": [\"DS_RADAR_01\", \"DS_LIDAR_01\"], \"duration\": \"2.8s\"}','{\"objects\": [{\"id\": 1, \"type\": \"car\", \"range\": 310.1, \"velocity\": 118}, {\"id\": 2, \"type\": \"car\", \"range\": 420.8, \"velocity\": 95}], \"lidarPointCount\": 42100}','2026-05-28 11:20:31',1),(2059950001000000005,'FR-20260528-005',2059946054681497600,'城市道路可见光-红外融合方案',3,890,2,'红外传感器信号中断，部分数据缺失','{\"sources\": [\"DS_VIS_CAM_01\"], \"duration\": \"1.1s\"}','{\"objects\": [{\"id\": 1, \"type\": \"vehicle\", \"confidence\": 0.71}], \"infraredEnhanced\": false}','2026-05-28 12:45:09',1),(2059950001000000006,'FR-20260528-006',2059946055377752064,'高速公路雷达-激光雷达融合方案',5,4100,1,NULL,'{\"sources\": [\"DS_RADAR_01\", \"DS_LIDAR_01\"], \"duration\": \"3.5s\"}','{\"objects\": [{\"id\": 1, \"type\": \"car\", \"range\": 155.3, \"velocity\": 145}, {\"id\": 2, \"type\": \"bus\", \"range\": 265.0, \"velocity\": 88}], \"lidarPointCount\": 55300}','2026-05-28 14:10:55',1),(2059950001000000007,'FR-20260528-007',2059946054681497600,'城市道路可见光-红外融合方案',3,1340,1,NULL,'{\"sources\": [\"DS_VIS_CAM_01\", \"DS_IR_CAM_01\"], \"duration\": \"2.1s\"}','{\"objects\": [{\"id\": 1, \"type\": \"pedestrian\", \"confidence\": 0.88}, {\"id\": 2, \"type\": \"cyclist\", \"confidence\": 0.82}], \"nightMode\": true, \"infraredEnhanced\": true}','2026-05-28 20:33:27',1),(2059950001000000008,'FR-20260528-008',2059946055377752064,'高速公路雷达-激光雷达融合方案',5,1200,1,NULL,'{\"sources\": [\"DS_RADAR_01\", \"DS_LIDAR_01\"], \"duration\": \"2.2s\"}','{\"objects\": [{\"id\": 1, \"type\": \"car\", \"range\": 90.2, \"velocity\": 18}, {\"id\": 2, \"type\": \"car\", \"range\": 110.5, \"velocity\": 15}, {\"id\": 3, \"type\": \"truck\", \"range\": 130.0, \"velocity\": 12}, {\"id\": 4, \"type\": \"car\", \"range\": 145.3, \"velocity\": 20}, {\"id\": 5, \"type\": \"car\", \"range\": 160.8, \"velocity\": 22}, {\"id\": 6, \"type\": \"bus\", \"range\": 175.0, \"velocity\": 10}, {\"id\": 7, \"type\": \"car\", \"range\": 185.2, \"velocity\": 16}, {\"id\": 8, \"type\": \"car\", \"range\": 200.0, \"velocity\": 19}, {\"id\": 9, \"type\": \"truck\", \"range\": 215.5, \"velocity\": 11}, {\"id\": 10, \"type\": \"car\", \"range\": 225.0, \"velocity\": 17}, {\"id\": 11, \"type\": \"car\", \"range\": 240.3, \"velocity\": 14}], \"lidarPointCount\": 28900}','2026-05-28 17:55:42',1);
/*!40000 ALTER TABLE `fusion_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fusion_rule`
--

DROP TABLE IF EXISTS `fusion_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fusion_rule` (
  `id` bigint NOT NULL,
  `scheme_id` bigint NOT NULL COMMENT '所属方案ID',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `fusion_type` varchar(20) NOT NULL COMMENT 'WEIGHTED/VOTE/PRIORITY',
  `fusion_fields` json DEFAULT NULL COMMENT '融合字段列表（JSON）',
  `trigger_condition` text COMMENT '触发条件表达式',
  `sort` int NOT NULL DEFAULT '0' COMMENT '执行顺序',
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_scheme_id` (`scheme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='融合规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fusion_rule`
--

LOCK TABLES `fusion_rule` WRITE;
/*!40000 ALTER TABLE `fusion_rule` DISABLE KEYS */;
INSERT INTO `fusion_rule` VALUES (2059946178212139008,2059946054681497600,'可见光红外像素级融合','PIXEL_LEVEL','[\"imageUrl\", \"heatmap\"]','光照强度<200lux OR 能见度<500m',1,1,0,'2026-05-28 18:33:35',1,'2026-05-28 18:33:35',NULL,NULL),(2059946179218771968,2059946054681497600,'目标特征级融合','FEATURE_LEVEL','[\"resolution\", \"minTemp\", \"maxTemp\"]','目标置信度<0.7',2,1,0,'2026-05-28 18:33:35',1,'2026-05-28 18:33:35',NULL,NULL),(2059946180175073280,2059946055377752064,'雷达LiDAR目标关联融合','DECISION_LEVEL','[\"targets\", \"range\", \"velocity\", \"pointCloud\"]','目标速度>60km/h',1,1,0,'2026-05-28 18:33:35',1,'2026-05-28 18:33:35',NULL,NULL);
/*!40000 ALTER TABLE `fusion_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fusion_scheme`
--

DROP TABLE IF EXISTS `fusion_scheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fusion_scheme` (
  `id` bigint NOT NULL,
  `scheme_code` varchar(32) NOT NULL COMMENT '方案编号（系统生成）',
  `scheme_name` varchar(100) NOT NULL COMMENT '方案名称',
  `scene_types` json DEFAULT NULL COMMENT '参与场景列表（JSON数组）',
  `fusion_goal` varchar(500) DEFAULT NULL COMMENT '融合目标描述',
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scheme_code` (`scheme_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='融合方案表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fusion_scheme`
--

LOCK TABLES `fusion_scheme` WRITE;
/*!40000 ALTER TABLE `fusion_scheme` DISABLE KEYS */;
INSERT INTO `fusion_scheme` VALUES (2059946054681497600,'FS_URBAN_VIS_IR','城市道路可见光-红外融合方案','[\"URBAN_ROAD\"]','融合可见光与红外图像，提升夜间及雨雾天气目标检测精度',1,0,'2026-05-28 18:33:06',1,'2026-05-28 18:33:06',NULL,'适用于城市道路交叉口监控场景'),(2059946055377752064,'FS_HW_RADAR_LIDAR','高速公路雷达-激光雷达融合方案','[\"HIGHWAY\"]','融合毫米波雷达与LiDAR数据，实现高速目标精确测速与定位',1,0,'2026-05-28 18:33:06',1,'2026-05-28 18:33:06',NULL,'适用于高速公路车辆流量监控和超速检测'),(2059946055839125504,'FS_AIRPORT_ALL','机场全要素融合方案','[\"AIRPORT\"]','融合可见光、红外、雷达、气象数据，构建机场全域态势感知',0,0,'2026-05-28 18:33:06',1,'2026-05-28 18:33:06',NULL,'试验性方案，尚未正式启用'),(2059947828754649088,'FS_TEST_TMP','已编辑临时方案','[\"HIGHWAY\"]','更新后的目标',0,1,'2026-05-28 18:40:09',1,'2026-05-28 18:40:09',1,NULL);
/*!40000 ALTER TABLE `fusion_scheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fusion_weight`
--

DROP TABLE IF EXISTS `fusion_weight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fusion_weight` (
  `id` bigint NOT NULL,
  `scheme_id` bigint NOT NULL COMMENT '所属方案ID',
  `rule_id` bigint NOT NULL COMMENT '所属规则ID',
  `ds_id` bigint NOT NULL COMMENT '数据源ID',
  `weight_value` decimal(5,2) NOT NULL COMMENT '权重值(0.00~1.00)',
  `adjust_reason` varchar(200) DEFAULT NULL COMMENT '调整原因',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_scheme_rule` (`scheme_id`,`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='融合权重配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fusion_weight`
--

LOCK TABLES `fusion_weight` WRITE;
/*!40000 ALTER TABLE `fusion_weight` DISABLE KEYS */;
INSERT INTO `fusion_weight` VALUES (2059946236361969664,2059946054681497600,2059946178212139008,2059945617949593600,0.60,'白天可见光质量好，权重较高','2026-05-28 18:33:49',1),(2059946236382941184,2059946054681497600,2059946178212139008,2059945618767482880,0.40,'辅助红外通道','2026-05-28 18:33:49',1);
/*!40000 ALTER TABLE `fusion_weight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL,
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父节点ID',
  `menu_name` varchar(64) NOT NULL COMMENT '菜单名称',
  `menu_type` char(1) NOT NULL COMMENT 'M目录 C菜单 F按钮',
  `path` varchar(200) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (100,0,'基础管理','M','/system',NULL,'setting',NULL,1,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(200,0,'数据源管理','M','/datasource',NULL,'database',NULL,2,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(300,0,'融合配置','M','/fusion',NULL,'merge',NULL,3,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(400,0,'决策规则','M','/decision',NULL,'rule',NULL,4,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(500,0,'融合结果','M','/fusion-result',NULL,'result',NULL,5,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(600,0,'决策结果','M','/decision-result',NULL,'check',NULL,6,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(700,0,'报表分析','M','/stats',NULL,'chart',NULL,7,1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL,
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) NOT NULL COMMENT '角色标识',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','SUPER_ADMIN','拥有全部权限',1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(2,'融合配置工程师','FUSION_ENG','配置数据源、融合规则与决策规则',1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(3,'决策分析员','ANALYST','查看融合/决策结果，导出报告',1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL),(4,'只读用户','READONLY','只读查看融合结果与报表',1,0,'2026-05-27 10:50:13',0,'2026-05-27 10:50:13',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '用户ID（雪花）',
  `username` varchar(64) NOT NULL COMMENT '登录账号',
  `real_name` varchar(64) NOT NULL COMMENT '姓名',
  `password` varchar(128) NOT NULL COMMENT '密码（BCrypt）',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常 1-删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','系统管理员','$2a$10$e.Xo8PiYQPT02kLJeLRGUO4WKqw8ta/lMMUqQbWhhuIvxcRjxP80O','13800000000','信息技术部',1,0,'2026-05-27 10:50:13',0,'2026-05-28 18:39:27',1,NULL),(2059945454346571776,'fusion_eng','李明华','$2a$10$bTxOrF0Cb7x8RKDxOr6vUe/fz9BEfWSz03/g/0yezRJmpdJnVfniK','13811001100','传感融合部',1,0,'2026-05-28 18:30:42',1,'2026-05-28 18:30:42',NULL,NULL),(2059945455403536384,'decision_ana','王晓芸','$2a$10$M3qMWGvW67POTa4SXNIUjOphb6Os9pN0wSjvytG3TKoZxWrLq6yA2','13822002200','决策支持部',1,0,'2026-05-28 18:30:43',1,'2026-05-28 18:30:43',NULL,NULL),(2059945456187871232,'data_ops','张伟','$2a$10$yO1x71VT8ohNZAHkXYgdfuxPQtBrbXOGlrv9bkgiEQRK7jfCwkVF.','13833003300','数据运维部',1,0,'2026-05-28 18:30:43',1,'2026-05-28 18:30:43',NULL,NULL),(2059947823377551360,'test_tmp','已编辑临时用户','$2a$10$aVDc4zo32Yc4YrxtXBA1luqlBaPklf5fcDefJoC8yPqqb2k/bfmRW','13999999998','测试部',0,1,'2026-05-28 18:40:07',1,'2026-05-28 18:40:08',1,NULL);
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
-- Dumping routines for database 'sensor_fusion_decision_support_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:11:26
