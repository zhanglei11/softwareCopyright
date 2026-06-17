-- MySQL dump 10.13  Distrib 8.4.9, for macos14.8 (x86_64)
--
-- Host: 127.0.0.1    Database: angu_ai_service_platform_dev
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
-- Table structure for table `ai_call_log`
--

DROP TABLE IF EXISTS `ai_call_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_call_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `scene_id` bigint DEFAULT NULL,
  `model_id` bigint DEFAULT NULL,
  `conversation_id` bigint DEFAULT NULL,
  `prompt_tokens` int DEFAULT '0',
  `completion_tokens` int DEFAULT '0',
  `total_tokens` int DEFAULT '0',
  `latency_ms` bigint DEFAULT '0',
  `success` tinyint DEFAULT '1',
  `error_msg` varchar(512) DEFAULT NULL,
  `call_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI调用日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_call_log`
--

LOCK TABLES `ai_call_log` WRITE;
/*!40000 ALTER TABLE `ai_call_log` DISABLE KEYS */;
INSERT INTO `ai_call_log` VALUES (1,1,1,1,1,120,280,400,1230,1,NULL,'2026-05-27 19:31:56'),(2,1,1,1,1,95,210,305,980,1,NULL,'2026-05-26 19:31:56'),(3,1,2,2,1,340,520,860,2100,1,NULL,'2026-05-26 19:31:56'),(4,1,1,1,1,88,195,283,860,1,NULL,'2026-05-25 19:31:56'),(5,1,3,3,1,450,630,1080,2800,1,NULL,'2026-05-25 19:31:56'),(6,1,5,4,1,200,310,510,1450,1,NULL,'2026-05-24 19:31:56'),(7,1,2,2,1,275,480,755,1890,1,NULL,'2026-05-24 19:31:56'),(8,1,1,1,1,110,240,350,1100,1,NULL,'2026-05-23 19:31:56'),(9,1,4,1,1,190,350,540,1560,1,NULL,'2026-05-23 19:31:56'),(10,1,3,3,1,380,590,970,2650,1,NULL,'2026-05-22 19:31:56'),(11,1,1,1,1,75,180,255,750,1,NULL,'2026-05-22 19:31:56'),(12,1,5,4,1,220,330,550,1380,1,NULL,'2026-05-21 19:31:56'),(13,1,2,2,1,310,510,820,2050,1,NULL,'2026-05-20 19:31:56'),(14,1,1,1,1,145,295,440,1280,1,NULL,'2026-05-19 19:31:56'),(15,1,3,3,1,420,610,1030,2750,1,NULL,'2026-05-17 19:31:56'),(16,1,4,1,1,160,320,480,1430,1,NULL,'2026-05-15 19:31:56'),(17,1,1,1,1,98,215,313,920,1,NULL,'2026-05-12 19:31:56'),(18,1,2,2,1,290,495,785,1950,1,NULL,'2026-05-09 19:31:56'),(19,1,5,4,1,185,295,480,1320,1,NULL,'2026-05-07 19:31:56'),(20,1,1,1,1,130,260,390,1050,1,NULL,'2026-05-02 19:31:56');
/*!40000 ALTER TABLE `ai_call_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_conversation`
--

DROP TABLE IF EXISTS `ai_conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `scene_id` bigint NOT NULL,
  `title` varchar(255) DEFAULT '新对话',
  `deleted` tinyint DEFAULT '0',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对话';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_conversation`
--

LOCK TABLES `ai_conversation` WRITE;
/*!40000 ALTER TABLE `ai_conversation` DISABLE KEYS */;
INSERT INTO `ai_conversation` VALUES (1,1,1,'新对话 2026-05-27T19:23',0,'2026-05-27 19:23:27','2026-05-27 19:23:27');
/*!40000 ALTER TABLE `ai_conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_message`
--

DROP TABLE IF EXISTS `ai_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conversation_id` bigint NOT NULL,
  `role` varchar(16) NOT NULL COMMENT 'USER/ASSISTANT/SYSTEM',
  `content` mediumtext,
  `token_count` int DEFAULT '0',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对话消息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_message`
--

LOCK TABLES `ai_message` WRITE;
/*!40000 ALTER TABLE `ai_message` DISABLE KEYS */;
INSERT INTO `ai_message` VALUES (1,1,'USER','帮我写一篇关于人工智能未来发展的文章摘要',NULL,'2026-05-27 19:23:27'),(2,1,'ASSISTANT','【AI回复占位】您发送了：帮我写一篇关于人工智能未来发展的文章摘要',NULL,'2026-05-27 19:23:28');
/*!40000 ALTER TABLE `ai_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_model_config`
--

DROP TABLE IF EXISTS `ai_model_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_model_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `model_name` varchar(64) NOT NULL,
  `model_id` varchar(128) NOT NULL COMMENT '模型ID/编码',
  `provider` varchar(64) DEFAULT NULL COMMENT '提供商',
  `api_url` varchar(512) DEFAULT NULL,
  `api_key_encrypted` varchar(512) DEFAULT NULL COMMENT 'Base64编码的API Key',
  `max_context_tokens` int DEFAULT '4096',
  `status` tinyint DEFAULT '1',
  `remark` varchar(512) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI模型配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_model_config`
--

LOCK TABLES `ai_model_config` WRITE;
/*!40000 ALTER TABLE `ai_model_config` DISABLE KEYS */;
INSERT INTO `ai_model_config` VALUES (1,'GPT-4o','gpt-4o','OpenAI','https://api.openai.com/v1',NULL,NULL,1,'OpenAI 最新旗舰模型，支持视觉理解，上下文128K','2026-05-27 19:12:14','2026-05-27 19:12:14'),(2,'Claude 3.5 Sonnet','claude-3-5-sonnet-20241022','Anthropic','https://api.anthropic.com/v1',NULL,NULL,1,'Anthropic最新旗舰模型，强大的推理和代码能力，200K上下文','2026-05-27 19:12:37','2026-05-27 19:12:37'),(3,'通义千问Max','qwen-max','阿里云','https://dashscope.aliyuncs.com/compatible-mode/v1',NULL,NULL,1,'阿里云通义千问最强版本，支持多轮对话和长文本','2026-05-27 19:12:37','2026-05-27 19:12:37'),(4,'DeepSeek-V3','deepseek-chat','DeepSeek','https://api.deepseek.com/v1',NULL,NULL,1,'DeepSeek最新模型，性能媲美GPT-4，国内性价比最高','2026-05-27 19:12:37','2026-05-27 19:16:36');
/*!40000 ALTER TABLE `ai_model_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_scene`
--

DROP TABLE IF EXISTS `ai_scene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_scene` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `description` text,
  `usage_guide` text,
  `model_id` bigint DEFAULT NULL,
  `kb_id` bigint DEFAULT NULL,
  `system_prompt` text,
  `user_prompt_tpl` text,
  `input_variables` text COMMENT 'JSON格式变量定义',
  `max_tokens` int DEFAULT '2048',
  `temperature` decimal(3,2) DEFAULT '0.70',
  `multi_turn` tinyint DEFAULT '1',
  `status` varchar(16) DEFAULT 'DRAFT' COMMENT 'DRAFT/ONLINE/OFFLINE',
  `deleted` tinyint DEFAULT '0',
  `creator_id` bigint DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI场景';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_scene`
--

LOCK TABLES `ai_scene` WRITE;
/*!40000 ALTER TABLE `ai_scene` DISABLE KEYS */;
INSERT INTO `ai_scene` VALUES (1,'AI写作助手',1,NULL,'智能文本创作助手，支持文章生成、润色、摘要等多种写作辅助功能',NULL,NULL,NULL,'你是一个专业的AI写作助手。',NULL,NULL,2048,0.70,NULL,'ONLINE',0,1,'2026-05-27 19:04:34','2026-05-27 19:07:49'),(2,'代码智能审查',2,NULL,'自动代码审查助手，识别代码缺陷、安全漏洞和优化建议',NULL,NULL,NULL,'你是一个专业的代码审查专家，精通多种编程语言。请对用户提供的代码进行全面分析，指出其中的问题、潜在缺陷、安全隐患，并给出优化建议。',NULL,NULL,2048,0.70,NULL,'ONLINE',0,1,'2026-05-27 19:06:16','2026-05-27 19:07:49'),(3,'数据报表分析',3,NULL,'智能数据报表分析，自动提取洞察并生成可视化建议',NULL,NULL,NULL,'你是数据分析专家，擅长解读各类数据报表。请帮助用户分析数据规律、发现业务洞察，并提出基于数据的决策建议。',NULL,NULL,2048,0.70,NULL,'ONLINE',0,1,'2026-05-27 19:06:16','2026-05-27 19:07:49'),(4,'客服问答机器人',4,NULL,'7x24小时智能客服，自动回答用户常见问题并引导解决',NULL,NULL,NULL,'你是一位友好耐心的客服专员，负责帮助用户解决问题。请保持专业、礼貌的语气，如无法解决问题，及时引导用户联系人工客服。',NULL,NULL,2048,0.70,NULL,'OFFLINE',0,1,'2026-05-27 19:06:17','2026-05-27 19:07:49'),(5,'多语言翻译助手',5,NULL,'支持50+语言互译，保留原文语境和专业术语',NULL,NULL,NULL,'你是专业的多语言翻译助手，精通中文、英文、日语、韩语、法语、德语等主流语言。翻译时要准确传达原文语义，保留专业术语，使译文自然流畅。',NULL,NULL,2048,0.70,NULL,'ONLINE',0,1,'2026-05-27 19:06:17','2026-05-27 19:10:02');
/*!40000 ALTER TABLE `ai_scene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_scene_category`
--

DROP TABLE IF EXISTS `ai_scene_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_scene_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `sort` int DEFAULT '0',
  `deleted` tinyint DEFAULT '0',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='场景分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_scene_category`
--

LOCK TABLES `ai_scene_category` WRITE;
/*!40000 ALTER TABLE `ai_scene_category` DISABLE KEYS */;
INSERT INTO `ai_scene_category` VALUES (1,'文本创作','edit',1,0,'2026-05-27 11:17:56','2026-05-27 11:17:56'),(2,'代码助手','code',2,0,'2026-05-27 11:17:56','2026-05-27 11:17:56'),(3,'数据分析','bar-chart',3,0,'2026-05-27 11:17:56','2026-05-27 11:17:56'),(4,'客服问答','customer-service',4,0,'2026-05-27 11:17:56','2026-05-27 11:17:56'),(5,'智能翻译','translation',5,0,'2026-05-27 19:01:23','2026-05-27 19:01:23');
/*!40000 ALTER TABLE `ai_scene_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kb_document`
--

DROP TABLE IF EXISTS `kb_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kb_document` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `kb_id` bigint NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_path` varchar(512) DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `file_type` varchar(32) DEFAULT NULL,
  `parse_status` varchar(16) DEFAULT 'PENDING' COMMENT 'PENDING/PROCESSING/DONE/FAILED',
  `chunk_count` int DEFAULT '0',
  `error_msg` varchar(512) DEFAULT NULL,
  `deleted` tinyint DEFAULT '0',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='知识库文档';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kb_document`
--

LOCK TABLES `kb_document` WRITE;
/*!40000 ALTER TABLE `kb_document` DISABLE KEYS */;
INSERT INTO `kb_document` VALUES (1,1,'产品使用手册.txt','/Users/zhanglei/angu-kb-uploads/1/1779881320742_产品使用手册.txt',935,'txt','PENDING',0,NULL,0,'2026-05-27 19:28:40','2026-05-27 19:28:40'),(2,1,'FAQ常见问题.txt','/Users/zhanglei/angu-kb-uploads/1/1779881371080_FAQ常见问题.txt',389,'txt','PENDING',0,NULL,0,'2026-05-27 19:29:31','2026-05-27 19:29:31'),(3,2,'技术架构说明.txt','/Users/zhanglei/angu-kb-uploads/2/1779881371171_技术架构说明.txt',461,'txt','PENDING',0,NULL,0,'2026-05-27 19:29:31','2026-05-27 19:29:31'),(4,3,'客服话术手册.txt','/Users/zhanglei/angu-kb-uploads/3/1779881371253_客服话术手册.txt',637,'txt','PENDING',0,NULL,0,'2026-05-27 19:29:31','2026-05-27 19:29:31'),(5,1,'FAQ常见问题.txt','/Users/zhanglei/angu-kb-uploads/1/1779881393441_FAQ常见问题.txt',389,'txt','PENDING',0,NULL,0,'2026-05-27 19:29:53','2026-05-27 19:29:53');
/*!40000 ALTER TABLE `kb_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kb_knowledge_base`
--

DROP TABLE IF EXISTS `kb_knowledge_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kb_knowledge_base` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `doc_count` int DEFAULT '0',
  `deleted` tinyint DEFAULT '0',
  `creator_id` bigint DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='知识库';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kb_knowledge_base`
--

LOCK TABLES `kb_knowledge_base` WRITE;
/*!40000 ALTER TABLE `kb_knowledge_base` DISABLE KEYS */;
INSERT INTO `kb_knowledge_base` VALUES (1,'产品知识库','公司产品文档、使用手册和FAQ汇总',3,0,1,'2026-05-27 19:26:58','2026-05-27 19:29:53'),(2,'技术文档库','开发规范、API文档和架构设计文档',1,0,1,'2026-05-27 19:26:58','2026-05-27 19:29:31'),(3,'客服知识库','客户服务话术、常见问题解答和处理流程',1,0,1,'2026-05-27 19:26:58','2026-05-27 19:29:31');
/*!40000 ALTER TABLE `kb_knowledge_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) NOT NULL,
  `parent_id` bigint DEFAULT '0',
  `menu_type` char(1) DEFAULT 'M' COMMENT 'D目录 M菜单 B按钮',
  `path` varchar(255) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `perms` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT NULL,
  `sort` int DEFAULT '0',
  `visible` tinyint DEFAULT '1',
  `status` tinyint DEFAULT '1',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'用户列表',0,'F',NULL,NULL,'system:user:list',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(2,'用户查询',0,'F',NULL,NULL,'system:user:query',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(3,'新增用户',0,'F',NULL,NULL,'system:user:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(4,'编辑用户',0,'F',NULL,NULL,'system:user:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(5,'删除用户',0,'F',NULL,NULL,'system:user:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(6,'角色列表',0,'F',NULL,NULL,'system:role:list',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(7,'新增角色',0,'F',NULL,NULL,'system:role:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(8,'编辑角色',0,'F',NULL,NULL,'system:role:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(9,'删除角色',0,'F',NULL,NULL,'system:role:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(10,'菜单列表',0,'F',NULL,NULL,'system:menu:list',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(11,'新增菜单',0,'F',NULL,NULL,'system:menu:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(12,'编辑菜单',0,'F',NULL,NULL,'system:menu:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(13,'删除菜单',0,'F',NULL,NULL,'system:menu:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(14,'场景列表',0,'F',NULL,NULL,'ai:scene:list',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(15,'场景查询',0,'F',NULL,NULL,'ai:scene:query',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(16,'新增场景',0,'F',NULL,NULL,'ai:scene:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(17,'编辑场景',0,'F',NULL,NULL,'ai:scene:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(18,'删除场景',0,'F',NULL,NULL,'ai:scene:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(19,'新增分类',0,'F',NULL,NULL,'ai:category:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(20,'编辑分类',0,'F',NULL,NULL,'ai:category:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(21,'删除分类',0,'F',NULL,NULL,'ai:category:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(22,'新增模型',0,'F',NULL,NULL,'ai:model:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(23,'编辑模型',0,'F',NULL,NULL,'ai:model:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(24,'知识库列表',0,'F',NULL,NULL,'kb:kb:list',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(25,'新增知识库',0,'F',NULL,NULL,'kb:kb:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(26,'编辑知识库',0,'F',NULL,NULL,'kb:kb:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(27,'删除知识库',0,'F',NULL,NULL,'kb:kb:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(28,'文档列表',0,'F',NULL,NULL,'kb:doc:list',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(29,'新增文档',0,'F',NULL,NULL,'kb:doc:add',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(30,'编辑文档',0,'F',NULL,NULL,'kb:doc:edit',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(31,'删除文档',0,'F',NULL,NULL,'kb:doc:delete',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(32,'看板统计',0,'F',NULL,NULL,'stats:dashboard:view',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(33,'场景统计',0,'F',NULL,NULL,'stats:scene:view',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42'),(34,'用户统计',0,'F',NULL,NULL,'stats:user:view',NULL,0,1,1,'2026-05-27 16:56:42','2026-05-27 16:56:42');
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
  `role_name` varchar(64) NOT NULL COMMENT '角色名',
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `builtin` tinyint DEFAULT '0' COMMENT '是否内置',
  `status` tinyint DEFAULT '1',
  `description` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','SUPER_ADMIN',1,1,'系统内置超级管理员','2026-05-27 11:17:56','2026-05-27 11:17:56'),(2,'AI管理员','AI_ADMIN',1,1,'系统内置AI管理员','2026-05-27 11:17:56','2026-05-27 11:17:56'),(3,'普通用户','NORMAL_USER',1,1,'系统内置普通用户','2026-05-27 11:17:56','2026-05-27 11:17:56');
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
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_scene`
--

DROP TABLE IF EXISTS `sys_role_scene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_scene` (
  `role_id` bigint NOT NULL,
  `scene_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`scene_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_scene`
--

LOCK TABLES `sys_role_scene` WRITE;
/*!40000 ALTER TABLE `sys_role_scene` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_scene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '登录账号',
  `real_name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `department` varchar(128) DEFAULT NULL COMMENT '部门',
  `password` varchar(255) NOT NULL COMMENT 'BCrypt密码',
  `daily_limit` int DEFAULT '100' COMMENT '每日调用上限',
  `status` tinyint DEFAULT '1' COMMENT '0禁用 1启用',
  `error_count` int DEFAULT '0' COMMENT '密码错误次数',
  `locked_until` datetime DEFAULT NULL COMMENT '锁定截止时间',
  `deleted` tinyint DEFAULT '0' COMMENT '0正常 1删除',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','超级管理员',NULL,NULL,NULL,'$2b$10$EWkwY0.c0rMULVKKxxiQpuMqEeByxxxo2gQBwe1L0tKBFgz7U3N7G',9999,1,0,NULL,0,'2026-05-27 11:17:56','2026-05-27 11:37:04'),(2,'zhangsan','张三','13800138001','zhangsan@example.com',NULL,'$2a$10$Dhz1JrcQ9T.iIgL1/mDJBO.XcCpOxeLfzZyiY9p/KZmh2LPi4f0Lm',100,1,0,NULL,0,'2026-05-27 19:34:10','2026-05-27 19:34:10'),(3,'wangwu','王五','13800138003','wangwu@example.com',NULL,'$2a$10$c91yoVDEQ6HLBbXYtTNKN.ATm5x3teo1vDW0KUT2Pd9MdBK0xMvGu',150,1,0,NULL,0,'2026-05-27 19:34:10','2026-05-27 19:34:10'),(4,'lisi001','李四','13800138002','lisi@example.com',NULL,'$2a$10$fiJUx/YFiXVqvfTkC4Y6w.SMMsHSo28.0tbwuM665e39.MHQWam.q',200,1,0,NULL,0,'2026-05-27 19:34:23','2026-05-27 19:34:23');
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
INSERT INTO `sys_user_role` VALUES (1,1),(2,3),(3,3),(4,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_scene_favorite`
--

DROP TABLE IF EXISTS `user_scene_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_scene_favorite` (
  `user_id` bigint NOT NULL,
  `scene_id` bigint NOT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`scene_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='场景收藏';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_scene_favorite`
--

LOCK TABLES `user_scene_favorite` WRITE;
/*!40000 ALTER TABLE `user_scene_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_scene_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'angu_ai_service_platform_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 10:05:58
