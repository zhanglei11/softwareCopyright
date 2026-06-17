-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: imaging_device_scheduler_dev
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
-- Table structure for table `device_fault_record`
--

DROP TABLE IF EXISTS `device_fault_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_fault_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_id` bigint NOT NULL,
  `fault_type` varchar(64) DEFAULT NULL COMMENT '故障类型',
  `fault_desc` varchar(500) DEFAULT NULL COMMENT '故障描述',
  `fault_time` datetime DEFAULT NULL COMMENT '故障时间',
  `resolved_time` datetime DEFAULT NULL COMMENT '解决时间',
  `resolved_by` bigint DEFAULT NULL COMMENT '处理人ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备故障记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_fault_record`
--

LOCK TABLES `device_fault_record` WRITE;
/*!40000 ALTER TABLE `device_fault_record` DISABLE KEYS */;
INSERT INTO `device_fault_record` VALUES (1,11,'机械故障','传送带皮带断裂，无法正常传送工件','2026-05-28 09:15:00',NULL,NULL,'2026-05-28 09:15:00'),(2,11,'通信异常','设备心跳超时，控制系统失联','2026-05-20 14:32:00','2026-05-20 16:45:00',1,'2026-05-20 14:32:00'),(3,11,'传感器故障','张力传感器数值异常，偏差超出阈值','2026-05-12 10:05:00','2026-05-12 14:20:00',1,'2026-05-12 10:05:00'),(4,11,'机械故障','电机过热保护触发，自动停机','2026-05-05 16:40:00','2026-05-06 09:00:00',1,'2026-05-05 16:40:00'),(5,4,'程序异常','运动路径规划失败，碰撞检测告警','2026-05-26 11:20:00','2026-05-26 13:35:00',1,'2026-05-26 11:20:00'),(6,4,'通信异常','Profibus通信中断，无法接收控制指令','2026-05-18 09:45:00','2026-05-18 11:10:00',1,'2026-05-18 09:45:00'),(7,4,'传感器故障','力矩传感器超量程告警','2026-05-08 14:22:00','2026-05-08 16:00:00',1,'2026-05-08 14:22:00'),(8,7,'电源故障','高压发生器电源不稳定，输出电压波动','2026-05-24 08:30:00','2026-05-24 10:15:00',1,'2026-05-24 08:30:00'),(9,7,'散热异常','球管温度过高，自动保护降功率','2026-05-15 15:10:00','2026-05-15 17:30:00',1,'2026-05-15 15:10:00'),(10,3,'软件异常','点云数据处理崩溃，扫描任务中断','2026-05-27 13:05:00','2026-05-27 14:50:00',1,'2026-05-27 13:05:00'),(11,3,'光学故障','激光发射模块功率衰减，扫描精度下降','2026-05-16 10:30:00','2026-05-17 09:00:00',1,'2026-05-16 10:30:00'),(12,9,'通信异常','GigE网络丢包率过高，数据传输中断','2026-05-22 16:20:00','2026-05-22 17:45:00',1,'2026-05-22 16:20:00'),(13,9,'软件异常','固件版本冲突导致设备重启循环','2026-05-10 11:15:00','2026-05-11 08:30:00',1,'2026-05-10 11:15:00'),(14,1,'图像异常','CMOS传感器出现固定噪点，图像质量下降','2026-05-25 09:45:00','2026-05-25 11:30:00',1,'2026-05-25 09:45:00'),(15,5,'机械故障','减速器齿轮磨损，运动精度超差','2026-05-21 14:00:00','2026-05-22 10:00:00',1,'2026-05-21 14:00:00');
/*!40000 ALTER TABLE `device_fault_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_info`
--

DROP TABLE IF EXISTS `device_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_name` varchar(128) NOT NULL,
  `device_code` varchar(64) DEFAULT NULL COMMENT '设备编码',
  `device_type` varchar(64) DEFAULT NULL COMMENT '设备类型：CAMERA/SENSOR/RADAR等',
  `scene_id` bigint DEFAULT NULL COMMENT '所属场景',
  `ip_address` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `port` int DEFAULT NULL COMMENT '端口',
  `manufacturer` varchar(128) DEFAULT NULL COMMENT '制造商',
  `model` varchar(128) DEFAULT NULL COMMENT '型号',
  `specs` text COMMENT '规格参数JSON',
  `status` tinyint NOT NULL DEFAULT '3' COMMENT '1-在线 2-占用 3-离线 4-故障 5-维护',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_code` (`device_code`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_info`
--

LOCK TABLES `device_info` WRITE;
/*!40000 ALTER TABLE `device_info` DISABLE KEYS */;
INSERT INTO `device_info` VALUES (1,'工业相机-A01',NULL,'1',1,'192.168.1.101',NULL,NULL,'MV-CA013-10UC',NULL,1,0,1,NULL,'2026-05-28 17:35:46','2026-05-28 17:58:44'),(2,'工业相机-A02',NULL,'1',1,'192.168.1.102',NULL,NULL,'MV-CA050-10GC',NULL,1,0,1,NULL,'2026-05-28 17:35:46','2026-05-28 17:44:18'),(3,'3D扫描仪-A01',NULL,'3',1,'192.168.1.110',NULL,NULL,'HandySCAN700',NULL,1,0,1,NULL,'2026-05-28 17:35:47','2026-05-28 17:44:30'),(4,'机械臂-B01',NULL,'5',2,'192.168.2.101',NULL,NULL,'KUKA KR30',NULL,2,0,1,NULL,'2026-05-28 17:35:47','2026-05-28 17:44:29'),(5,'机械臂-B02',NULL,'5',2,'192.168.2.102',NULL,NULL,'KUKA KR30',NULL,2,0,1,NULL,'2026-05-28 17:35:47','2026-05-28 17:44:29'),(6,'工业相机-B03',NULL,'1',2,'192.168.2.103',NULL,NULL,'Basler acA4024',NULL,2,0,1,NULL,'2026-05-28 17:35:47','2026-05-28 17:44:29'),(7,'X光检测仪-C01',NULL,'2',5,'192.168.5.101',NULL,NULL,'Zeiss METROTOM',NULL,2,0,1,NULL,'2026-05-28 17:35:47','2026-05-28 17:44:29'),(8,'X光检测仪-C02',NULL,'2',5,'192.168.5.102',NULL,NULL,'Zeiss METROTOM',NULL,2,0,1,NULL,'2026-05-28 17:35:48','2026-05-28 17:44:29'),(9,'3D扫描仪-D01',NULL,'3',6,'192.168.6.101',NULL,NULL,'FARO Edge Arm',NULL,10,0,1,NULL,'2026-05-28 17:35:48','2026-05-28 17:58:44'),(10,'3D扫描仪-D02',NULL,'3',6,'192.168.6.102',NULL,NULL,'FARO Edge Arm',NULL,1,0,1,NULL,'2026-05-28 17:35:48','2026-05-28 17:44:19'),(11,'传送带-E01',NULL,'4',3,'192.168.3.101',NULL,NULL,'SEW EURODRIVE',NULL,4,0,1,NULL,'2026-05-28 17:35:48','2026-05-28 17:44:19'),(12,'工业相机-F01',NULL,'1',4,'192.168.4.101',NULL,NULL,'IDS uEye FA',NULL,2,0,1,NULL,'2026-05-28 17:35:48','2026-05-28 17:44:43'),(13,'测试相机X01',NULL,'1',1,'192.168.1.200',NULL,NULL,'MV-CA013',NULL,3,1,1,NULL,'2026-05-28 17:58:31','2026-05-28 17:59:40');
/*!40000 ALTER TABLE `device_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_param`
--

DROP TABLE IF EXISTS `device_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_param` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_id` bigint NOT NULL,
  `param_key` varchar(128) NOT NULL,
  `param_value` varchar(1024) DEFAULT NULL,
  `param_desc` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_param` (`device_id`,`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_param`
--

LOCK TABLES `device_param` WRITE;
/*!40000 ALTER TABLE `device_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispatch_config`
--

DROP TABLE IF EXISTS `dispatch_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispatch_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `max_devices_per_task` int NOT NULL DEFAULT '5' COMMENT '每任务最大设备数',
  `task_timeout_minutes` int NOT NULL DEFAULT '120' COMMENT '任务超时分钟',
  `auto_dispatch_enabled` tinyint NOT NULL DEFAULT '0' COMMENT '是否启用自动调度',
  `dispatch_strategy` varchar(64) NOT NULL DEFAULT 'MANUAL' COMMENT '调度策略',
  `alert_threshold_minutes` int NOT NULL DEFAULT '30' COMMENT '预警阈值分钟',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispatch_config`
--

LOCK TABLES `dispatch_config` WRITE;
/*!40000 ALTER TABLE `dispatch_config` DISABLE KEYS */;
INSERT INTO `dispatch_config` VALUES (1,5,120,0,'MANUAL',30,'2026-05-28 17:54:36');
/*!40000 ALTER TABLE `dispatch_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispatch_log`
--

DROP TABLE IF EXISTS `dispatch_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispatch_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `action` varchar(64) NOT NULL COMMENT '操作动作',
  `action_desc` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `device_ids` varchar(500) DEFAULT NULL COMMENT '涉及设备IDs（逗号分隔）',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) DEFAULT NULL COMMENT '操作人姓名',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispatch_log`
--

LOCK TABLES `dispatch_log` WRITE;
/*!40000 ALTER TABLE `dispatch_log` DISABLE KEYS */;
INSERT INTO `dispatch_log` VALUES (1,1,'CREATE',NULL,NULL,1,NULL,'2026-05-28 17:36:27'),(2,2,'CREATE',NULL,NULL,1,NULL,'2026-05-28 17:36:27'),(3,3,'CREATE',NULL,NULL,1,NULL,'2026-05-28 17:36:27'),(4,4,'CREATE',NULL,NULL,1,NULL,'2026-05-28 17:36:28'),(5,5,'CREATE',NULL,NULL,1,NULL,'2026-05-28 17:36:28'),(6,6,'CREATE',NULL,NULL,1,NULL,'2026-05-28 17:36:28'),(7,1,'ASSIGN',NULL,'1',1,NULL,'2026-05-28 17:44:29'),(8,1,'ASSIGN',NULL,'3',1,NULL,'2026-05-28 17:44:29'),(9,2,'ASSIGN',NULL,'4',1,NULL,'2026-05-28 17:44:29'),(10,2,'ASSIGN',NULL,'5',1,NULL,'2026-05-28 17:44:29'),(11,2,'ASSIGN',NULL,'6',1,NULL,'2026-05-28 17:44:29'),(12,3,'ASSIGN',NULL,'7',1,NULL,'2026-05-28 17:44:29'),(13,3,'ASSIGN',NULL,'8',1,NULL,'2026-05-28 17:44:29'),(14,1,'START',NULL,NULL,1,NULL,'2026-05-28 17:44:30'),(15,2,'START',NULL,NULL,1,NULL,'2026-05-28 17:44:30'),(16,3,'START',NULL,NULL,1,NULL,'2026-05-28 17:44:30'),(17,1,'COMPLETE',NULL,NULL,1,NULL,'2026-05-28 17:44:30'),(18,4,'ASSIGN',NULL,'12',1,NULL,'2026-05-28 17:44:43'),(19,6,'CANCEL',NULL,NULL,1,NULL,'2026-05-28 17:44:43');
/*!40000 ALTER TABLE `dispatch_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scene_group`
--

DROP TABLE IF EXISTS `scene_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scene_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(128) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='场景分组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scene_group`
--

LOCK TABLES `scene_group` WRITE;
/*!40000 ALTER TABLE `scene_group` DISABLE KEYS */;
INSERT INTO `scene_group` VALUES (1,'生产线A区','A区影像采集',0,0,NULL,NULL,'2026-05-28 17:29:25','2026-05-28 17:29:25'),(2,'生产线B区','B区影像采集',0,0,NULL,NULL,'2026-05-28 17:29:25','2026-05-28 17:29:25'),(3,'质检中心','成品质量检测',0,0,NULL,NULL,'2026-05-28 17:29:25','2026-05-28 17:29:25'),(4,'仓储区域','仓储货物监测',0,0,NULL,NULL,'2026-05-28 17:29:25','2026-05-28 17:29:25');
/*!40000 ALTER TABLE `scene_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scene_info`
--

DROP TABLE IF EXISTS `scene_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scene_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scene_name` varchar(128) NOT NULL,
  `scene_code` varchar(64) DEFAULT NULL COMMENT '场景编码',
  `group_id` bigint DEFAULT NULL COMMENT '所属分组',
  `location` varchar(255) DEFAULT NULL COMMENT '位置描述',
  `description` varchar(500) DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0-停用 1-启用',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `scene_code` (`scene_code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='场景信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scene_info`
--

LOCK TABLES `scene_info` WRITE;
/*!40000 ALTER TABLE `scene_info` DISABLE KEYS */;
INSERT INTO `scene_info` VALUES (1,'精密加工区',NULL,1,NULL,'高精度零件加工作业区域',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:50','2026-05-28 17:59:49'),(2,'组装流水线',NULL,1,NULL,'产品组装流水线场景',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:51','2026-05-28 17:33:51'),(3,'表面处理区',NULL,2,NULL,'零件表面处理作业',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:51','2026-05-28 17:33:51'),(4,'焊接车间',NULL,2,NULL,'工业焊接作业场景',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:51','2026-05-28 17:33:51'),(5,'外观质检线',NULL,3,NULL,'产品外观质量检测',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:51','2026-05-28 17:33:51'),(6,'尺寸精度检测',NULL,3,NULL,'高精度尺寸测量场景',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:51','2026-05-28 17:33:51'),(7,'成品仓A',NULL,4,NULL,'成品入库存储区域',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:52','2026-05-28 17:33:52'),(8,'原料仓B',NULL,4,NULL,'原材料存储管理区域',NULL,NULL,1,0,NULL,NULL,'2026-05-28 17:33:52','2026-05-28 17:33:52'),(9,'测试新场景',NULL,1,NULL,'测试场景描述',NULL,NULL,1,1,NULL,NULL,'2026-05-28 17:58:31','2026-05-28 17:59:40');
/*!40000 ALTER TABLE `scene_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父ID，0为根',
  `menu_name` varchar(64) NOT NULL,
  `menu_type` tinyint DEFAULT NULL COMMENT '1-目录 2-菜单 3-按钮',
  `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `perms` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `status` tinyint NOT NULL DEFAULT '1',
  `sort_order` int DEFAULT '0',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'系统管理',1,NULL,NULL,NULL,NULL,1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(2,1,'用户管理',2,NULL,NULL,NULL,NULL,1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(3,2,'查询用户',3,NULL,NULL,NULL,'system:user:list',1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(4,2,'新增用户',3,NULL,NULL,NULL,'system:user:add',1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(5,2,'编辑用户',3,NULL,NULL,NULL,'system:user:edit',1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(6,2,'删除用户',3,NULL,NULL,NULL,'system:user:delete',1,4,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(7,1,'角色管理',2,NULL,NULL,NULL,NULL,1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(8,7,'查询角色',3,NULL,NULL,NULL,'system:role:list',1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(9,7,'新增角色',3,NULL,NULL,NULL,'system:role:add',1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(10,7,'编辑角色',3,NULL,NULL,NULL,'system:role:edit',1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(11,7,'删除角色',3,NULL,NULL,NULL,'system:role:delete',1,4,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(12,0,'场景管理',1,NULL,NULL,NULL,NULL,1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(13,12,'场景列表',2,NULL,NULL,NULL,NULL,1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(14,13,'查询场景',3,NULL,NULL,NULL,'scene:info:list',1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(15,13,'新增场景',3,NULL,NULL,NULL,'scene:info:add',1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(16,13,'编辑场景',3,NULL,NULL,NULL,'scene:info:edit',1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(17,13,'删除场景',3,NULL,NULL,NULL,'scene:info:delete',1,4,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(18,0,'设备管理',1,NULL,NULL,NULL,NULL,1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(19,18,'设备列表',2,NULL,NULL,NULL,NULL,1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(20,19,'查询设备',3,NULL,NULL,NULL,'device:info:list',1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(21,19,'新增设备',3,NULL,NULL,NULL,'device:info:add',1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(22,19,'编辑设备',3,NULL,NULL,NULL,'device:info:edit',1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(23,19,'删除设备',3,NULL,NULL,NULL,'device:info:delete',1,4,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(24,0,'任务调度',1,NULL,NULL,NULL,NULL,1,4,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(25,24,'任务列表',2,NULL,NULL,NULL,NULL,1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(26,25,'查询任务',3,NULL,NULL,NULL,'task:info:list',1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(27,25,'新增任务',3,NULL,NULL,NULL,'task:info:add',1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(28,25,'调度操作',3,NULL,NULL,NULL,'task:info:dispatch',1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(29,25,'取消任务',3,NULL,NULL,NULL,'task:info:cancel',1,4,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(30,25,'删除任务',3,NULL,NULL,NULL,'task:info:delete',1,5,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(31,24,'调度总览',2,NULL,NULL,NULL,'dispatch:overview:view',1,2,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(32,24,'甘特图',2,NULL,NULL,NULL,'dispatch:gantt:view',1,3,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(33,0,'统计分析',1,NULL,NULL,NULL,NULL,1,5,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(34,33,'统计查看',2,NULL,NULL,NULL,'statistics:view',1,1,0,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(35,0,'场景分组-查询',3,NULL,NULL,NULL,'scene:group:list',1,1,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(36,0,'场景分组-新增',3,NULL,NULL,NULL,'scene:group:add',1,2,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(37,0,'场景分组-编辑',3,NULL,NULL,NULL,'scene:group:edit',1,3,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(38,0,'场景分组-删除',3,NULL,NULL,NULL,'scene:group:delete',1,4,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(39,0,'设备参数-查询',3,NULL,NULL,NULL,'device:param:list',1,5,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(40,0,'设备参数-编辑',3,NULL,NULL,NULL,'device:param:edit',1,6,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(41,0,'调度-超时告警',3,NULL,NULL,NULL,'dispatch:alert:view',1,7,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(42,0,'调度-配置查看',3,NULL,NULL,NULL,'dispatch:config:view',1,8,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(43,0,'调度-配置编辑',3,NULL,NULL,NULL,'dispatch:config:edit',1,9,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(44,0,'调度-日志列表',3,NULL,NULL,NULL,'dispatch:log:list',1,10,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(45,0,'调度-日志导出',3,NULL,NULL,NULL,'dispatch:log:export',1,11,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(46,0,'菜单-查询',3,NULL,NULL,NULL,'system:menu:list',1,12,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(47,0,'菜单-新增',3,NULL,NULL,NULL,'system:menu:add',1,13,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(48,0,'菜单-编辑',3,NULL,NULL,NULL,'system:menu:edit',1,14,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(49,0,'菜单-删除',3,NULL,NULL,NULL,'system:menu:delete',1,15,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(50,0,'任务-分配设备',3,NULL,NULL,NULL,'task:info:assign',1,16,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(51,0,'任务-开始',3,NULL,NULL,NULL,'task:info:start',1,17,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(52,0,'任务-完成',3,NULL,NULL,NULL,'task:info:complete',1,18,0,'2026-05-28 17:18:38','2026-05-28 17:18:38'),(53,0,'任务-编辑',3,NULL,NULL,NULL,'task:info:edit',1,19,0,'2026-05-28 17:18:38','2026-05-28 17:18:38');
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
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
  `sort_order` int DEFAULT '0',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','SUPER_ADMIN','拥有所有权限',1,1,0,NULL,NULL,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(2,'调度管理员','DISPATCH_ADMIN','负责任务调度',1,2,0,NULL,NULL,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(3,'设备运维','DEVICE_OPS','负责设备管理',1,3,0,NULL,NULL,'2026-05-27 09:59:59','2026-05-27 09:59:59'),(4,'普通操作员','OPERATOR','只读查看权限',1,4,0,NULL,NULL,'2026-05-27 09:59:59','2026-05-27 09:59:59');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,1,7),(8,1,8),(9,1,9),(10,1,10),(11,1,11),(12,1,12),(13,1,13),(14,1,14),(15,1,15),(16,1,16),(17,1,17),(18,1,18),(19,1,19),(20,1,20),(21,1,21),(22,1,22),(23,1,23),(24,1,24),(25,1,25),(26,1,26),(27,1,27),(28,1,28),(29,1,29),(30,1,30),(31,1,31),(32,1,32),(33,1,33),(34,1,34),(64,1,35),(65,1,36),(66,1,37),(67,1,38),(68,1,39),(69,1,40),(70,1,41),(71,1,42),(72,1,43),(73,1,44),(74,1,45),(75,1,46),(76,1,47),(77,1,48),(78,1,49),(79,1,50),(80,1,51),(81,1,52),(82,1,53);
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
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT 'BCrypt密码',
  `real_name` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `dept` varchar(128) DEFAULT NULL COMMENT '所属部门',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常 1-删除',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `updated_by` bigint DEFAULT NULL COMMENT '修改人ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2b$10$L6TVlBIWoZlrbS642igrM.zvcXhnqJ/S9tayblILRZPdRlcdCs4fC','系统管理员',NULL,NULL,'技术部',1,0,NULL,NULL,'2026-05-27 09:59:59','2026-05-28 17:16:07');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_device_rel`
--

DROP TABLE IF EXISTS `task_device_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_device_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL,
  `device_id` bigint NOT NULL,
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '1-有效 0-已撤销',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务设备关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_device_rel`
--

LOCK TABLES `task_device_rel` WRITE;
/*!40000 ALTER TABLE `task_device_rel` DISABLE KEYS */;
INSERT INTO `task_device_rel` VALUES (1,1,1,'2026-05-28 17:44:29',0),(2,1,3,'2026-05-28 17:44:29',0),(3,2,4,'2026-05-28 17:44:29',1),(4,2,5,'2026-05-28 17:44:29',1),(5,2,6,'2026-05-28 17:44:29',1),(6,3,7,'2026-05-28 17:44:29',1),(7,3,8,'2026-05-28 17:44:29',1),(8,4,12,'2026-05-28 17:44:43',1),(9,7,7,'2026-05-22 07:58:00',0),(10,7,8,'2026-05-22 07:58:00',0),(11,8,9,'2026-05-22 13:48:00',0),(12,8,10,'2026-05-22 13:48:00',0),(13,9,1,'2026-05-23 07:48:00',0),(14,9,2,'2026-05-23 07:48:00',0),(15,9,3,'2026-05-23 07:48:00',0),(16,10,4,'2026-05-23 08:53:00',0),(17,10,5,'2026-05-23 08:53:00',0),(18,11,7,'2026-05-23 13:58:00',0),(19,12,4,'2026-05-24 07:53:00',0),(20,12,6,'2026-05-24 07:53:00',0),(21,13,11,'2026-05-24 09:58:00',0),(22,14,7,'2026-05-24 12:48:00',0),(23,14,8,'2026-05-24 12:48:00',0),(24,15,1,'2026-05-25 06:58:00',0),(25,15,2,'2026-05-25 06:58:00',0),(26,15,3,'2026-05-25 06:58:00',0),(27,16,4,'2026-05-25 08:53:00',0),(28,16,5,'2026-05-25 08:53:00',0),(29,17,9,'2026-05-25 12:53:00',0),(30,17,10,'2026-05-25 12:53:00',0),(31,18,12,'2026-05-25 14:58:00',0),(32,19,4,'2026-05-26 07:48:00',0),(33,19,12,'2026-05-26 07:48:00',0),(34,20,7,'2026-05-26 10:58:00',0),(35,20,8,'2026-05-26 10:58:00',0),(36,21,4,'2026-05-26 13:58:00',0),(37,21,5,'2026-05-26 13:58:00',0),(38,21,6,'2026-05-26 13:58:00',0),(39,22,1,'2026-05-27 07:53:00',0),(40,22,3,'2026-05-27 07:53:00',0),(41,23,4,'2026-05-27 08:58:00',0),(42,23,5,'2026-05-27 08:58:00',0),(43,23,6,'2026-05-27 08:58:00',0),(44,24,7,'2026-05-27 09:58:00',0),(45,24,8,'2026-05-27 09:58:00',0),(46,25,12,'2026-05-27 13:53:00',0);
/*!40000 ALTER TABLE `task_device_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_info`
--

DROP TABLE IF EXISTS `task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) NOT NULL,
  `task_code` varchar(64) DEFAULT NULL,
  `scene_id` bigint DEFAULT NULL COMMENT '关联场景',
  `priority` int NOT NULL DEFAULT '5' COMMENT '优先级1-10',
  `device_count` int NOT NULL DEFAULT '1' COMMENT '需要设备数',
  `plan_start_time` datetime DEFAULT NULL COMMENT '计划开始',
  `plan_end_time` datetime DEFAULT NULL COMMENT '计划结束',
  `actual_start_time` datetime DEFAULT NULL COMMENT '实际开始',
  `actual_end_time` datetime DEFAULT NULL COMMENT '实际结束',
  `description` varchar(1000) DEFAULT NULL,
  `status` int NOT NULL DEFAULT '10' COMMENT '10-待分配 20-已分配 30-执行中 50-已完成 -20-已取消',
  `deleted` tinyint NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_code` (`task_code`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_info`
--

LOCK TABLES `task_info` WRITE;
/*!40000 ALTER TABLE `task_info` DISABLE KEYS */;
INSERT INTO `task_info` VALUES (1,'精密零件尺寸检测',NULL,1,1,2,'2026-05-28 08:00:00','2026-05-28 12:00:00','2026-05-28 17:44:30','2026-05-28 17:44:30','对精密加工区零件进行尺寸精度检测',50,0,1,NULL,'2026-05-28 17:36:27','2026-05-28 17:44:30'),(2,'流水线组装质量巡检',NULL,2,2,3,'2026-05-28 09:00:00','2026-05-28 18:00:00','2026-05-28 17:44:30',NULL,'组装流水线全程质量监控',30,0,1,NULL,'2026-05-28 17:36:27','2026-05-28 17:44:30'),(3,'外观缺陷检测',NULL,5,1,2,'2026-05-28 10:00:00','2026-05-28 16:00:00','2026-05-28 17:44:30',NULL,'产品外观缺陷自动检测',30,0,1,NULL,'2026-05-28 17:36:27','2026-05-28 17:44:30'),(4,'焊缝质量检测',NULL,4,2,1,'2026-05-29 08:00:00','2026-05-29 14:00:00',NULL,NULL,'焊缝外观及强度检测',20,0,1,NULL,'2026-05-28 17:36:28','2026-05-28 17:44:43'),(5,'精度全检报告',NULL,6,1,2,'2026-05-29 09:00:00','2026-05-29 17:00:00',NULL,NULL,'批次产品精度全面检测',10,0,1,NULL,'2026-05-28 17:36:28','2026-05-28 17:36:28'),(6,'入库扫描建档',NULL,7,3,1,'2026-05-30 08:00:00','2026-05-30 12:00:00',NULL,NULL,'成品入库扫描建档',-20,0,1,NULL,'2026-05-28 17:36:28','2026-05-28 17:44:43'),(7,'质检扫描任务-0522A','T20260522001',5,2,2,'2026-05-22 08:00:00','2026-05-22 12:00:00',NULL,NULL,'外观质检',50,0,1,1,'2026-05-22 07:30:00','2026-05-22 12:05:00'),(8,'精度测量任务-0522B','T20260522002',6,1,2,'2026-05-22 14:00:00','2026-05-22 17:00:00',NULL,NULL,'尺寸精度检测',-20,0,1,1,'2026-05-22 13:45:00','2026-05-22 14:30:00'),(9,'加工监测任务-0523A','T20260523001',1,3,3,'2026-05-23 08:00:00','2026-05-23 11:00:00',NULL,NULL,'精密加工实时监测',50,0,1,1,'2026-05-23 07:45:00','2026-05-23 11:10:00'),(10,'组装质检任务-0523B','T20260523002',2,2,2,'2026-05-23 09:00:00','2026-05-23 13:00:00',NULL,NULL,'组装流水线质量检查',50,0,1,1,'2026-05-23 08:50:00','2026-05-23 13:05:00'),(11,'仓储盘点任务-0523C','T20260523003',7,1,1,'2026-05-23 14:00:00','2026-05-23 16:00:00',NULL,NULL,'成品仓盘点',-20,0,1,1,'2026-05-23 13:55:00','2026-05-23 16:30:00'),(12,'焊接检测任务-0524A','T20260524001',4,3,2,'2026-05-24 08:00:00','2026-05-24 11:00:00',NULL,NULL,'焊缝质量检测',50,0,1,1,'2026-05-24 07:50:00','2026-05-24 11:08:00'),(13,'表面处理监控-0524B','T20260524002',3,2,1,'2026-05-24 10:00:00','2026-05-24 14:00:00',NULL,NULL,'表面处理过程监控',50,0,1,1,'2026-05-24 09:55:00','2026-05-24 14:03:00'),(14,'X光探伤任务-0524C','T20260524003',5,2,2,'2026-05-24 13:00:00','2026-05-24 17:00:00',NULL,NULL,'X光无损探伤',30,0,1,1,'2026-05-24 12:45:00','2026-05-24 13:02:00'),(15,'精加工全检-0525A','T20260525001',1,3,3,'2026-05-25 07:00:00','2026-05-25 11:00:00',NULL,NULL,'全件精度检查',50,0,1,1,'2026-05-25 06:55:00','2026-05-25 11:12:00'),(16,'流水线巡检-0525B','T20260525002',2,2,2,'2026-05-25 09:00:00','2026-05-25 12:00:00',NULL,NULL,'流水线设备状态巡检',50,0,1,1,'2026-05-25 08:50:00','2026-05-25 12:05:00'),(17,'尺寸抽检-0525C','T20260525003',6,2,2,'2026-05-25 13:00:00','2026-05-25 16:00:00',NULL,NULL,'成品尺寸随机抽检',50,0,1,1,'2026-05-25 12:50:00','2026-05-25 16:08:00'),(18,'仓储入库扫描-0525D','T20260525004',8,1,1,'2026-05-25 15:00:00','2026-05-25 17:00:00',NULL,NULL,'原料入库二维码扫描',-20,0,1,1,'2026-05-25 14:55:00','2026-05-25 15:45:00'),(19,'焊缝全检-0526A','T20260526001',4,3,2,'2026-05-26 08:00:00','2026-05-26 10:00:00',NULL,NULL,'焊缝质量全面检测',50,0,1,1,'2026-05-26 07:45:00','2026-05-26 10:15:00'),(20,'质检外观-0526B','T20260526002',5,2,2,'2026-05-26 11:00:00','2026-05-26 14:00:00',NULL,NULL,'外观缺陷检测',50,0,1,1,'2026-05-26 10:55:00','2026-05-26 14:10:00'),(21,'机械臂校准-0526C','T20260526003',2,1,3,'2026-05-26 14:00:00','2026-05-26 17:00:00',NULL,NULL,'机械臂运动轨迹校准',-20,0,1,1,'2026-05-26 13:55:00','2026-05-26 14:50:00'),(22,'精密扫描-0527A','T20260527001',1,3,2,'2026-05-27 08:00:00','2026-05-27 11:00:00',NULL,NULL,'精密零件3D扫描',50,0,1,1,'2026-05-27 07:50:00','2026-05-27 11:05:00'),(23,'流水线监测-0527B','T20260527002',2,2,3,'2026-05-27 09:00:00','2026-05-27 13:00:00',NULL,NULL,'生产线实时监测',50,0,1,1,'2026-05-27 08:55:00','2026-05-27 13:08:00'),(24,'X光检测-0527C','T20260527003',5,2,2,'2026-05-27 10:00:00','2026-05-27 14:00:00',NULL,NULL,'X光无损检测',50,0,1,1,'2026-05-27 09:55:00','2026-05-27 14:12:00'),(25,'仓储盘点-0527D','T20260527004',7,1,1,'2026-05-27 14:00:00','2026-05-27 16:00:00',NULL,NULL,'成品出库核验',30,0,1,1,'2026-05-27 13:50:00','2026-05-27 14:05:00');
/*!40000 ALTER TABLE `task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'imaging_device_scheduler_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:09:25
