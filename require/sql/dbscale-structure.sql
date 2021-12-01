-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 192.168.49.21    Database: dbscale
-- ------------------------------------------------------
-- Server version	5.7.31-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `def_serv`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `def_serv` (
  `code` varchar(64) NOT NULL COMMENT '服务代码',
  `name` varchar(16) NOT NULL COMMENT '服务名称',
  `is_stateful` tinyint(1) NOT NULL,
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `sequence` int(11) unsigned NOT NULL COMMENT '显示顺序',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`code`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='服务定义表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_blob_triggers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_calendars`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_cron_triggers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_fired_triggers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_job_details`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_locks`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_scheduler_state`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_simple_triggers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_simprop_triggers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_triggers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_app`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_app` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL COMMENT '控件名',
  `type` varchar(32) NOT NULL COMMENT '控件类型',
  `code` varchar(64) NOT NULL COMMENT '控件代码',
  `icon` varchar(64) DEFAULT NULL COMMENT '控件图标',
  `pos` varchar(32) DEFAULT NULL COMMENT '按钮位置',
  `sequence` int(11) unsigned DEFAULT NULL COMMENT '显示顺序(菜单)',
  `tabletop_seq` int(11) unsigned DEFAULT NULL COMMENT '显示顺序(表头按钮)',
  `row_seq` int(11) DEFAULT NULL COMMENT '显示顺序(表格行内按钮)',
  `pid` int(11) unsigned NOT NULL COMMENT '父控件编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=905020001 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='控件信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_backup_strategy`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_backup_strategy` (
  `id` varchar(64) NOT NULL,
  `serv_group_id` varchar(64) NOT NULL COMMENT '服务组编码',
  `backup_storage_type` varchar(32) NOT NULL COMMENT '备份存储类型',
  `type` varchar(32) NOT NULL COMMENT '备份类型',
  `tables` longtext COMMENT '表信息',
  `cron_expression` varchar(64) NOT NULL COMMENT 'crontab表达式',
  `file_retention_num` int(11) unsigned NOT NULL COMMENT '备份文件保留份数',
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_serv_group_id&cron` (`serv_group_id`,`cron_expression`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='备份策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_business_area`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_business_area` (
  `id` varchar(64) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '业务区名称',
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `site_id` varchar(64) NOT NULL COMMENT '站点编码',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_site_id&name` (`site_id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='业务区表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_business_subsystem`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_business_subsystem` (
  `id` varchar(64) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '业务子系统名',
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `business_system_id` varchar(64) NOT NULL COMMENT '业务系统编码',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uk_system_id&name` (`business_system_id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='业务子系统';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_business_system`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_business_system` (
  `id` varchar(64) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '业务系统名',
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `owner` varchar(32) NOT NULL COMMENT '所属者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_owner&name` (`owner`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='业务系统表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_crontab_config`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_crontab_config` (
  `trigger_name` varchar(64) NOT NULL COMMENT '触发器名称',
  `crontab` varchar(64) NOT NULL COMMENT 'linux crontab表达式',
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`trigger_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='定时任务配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_dict`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_dict` (
  `dict_type_code` varchar(64) NOT NULL COMMENT '字典类型代码',
  `code` varchar(64) NOT NULL COMMENT '字典代码',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `sequence` int(11) unsigned NOT NULL COMMENT '显示顺序',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`dict_type_code`,`code`) USING BTREE,
  UNIQUE KEY `uk_dict_type_code&name` (`dict_type_code`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_dict_type`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_dict_type` (
  `code` varchar(64) NOT NULL COMMENT '类型代码',
  `name` varchar(32) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_force_rebuild_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_force_rebuild_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `unit_relate_id` varchar(64) NOT NULL,
  `source_host_relate_id` varchar(64) NOT NULL,
  `target_host_relate_id` varchar(64) DEFAULT NULL,
  `task_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_group`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_group` (
  `id` varchar(64) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '组名',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_sys` tinyint(1) unsigned NOT NULL COMMENT '是否为系统资源',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_name&creator` (`name`,`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='组别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_group_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_group_user` (
  `group_id` varchar(64) NOT NULL,
  `username` varchar(32) NOT NULL,
  PRIMARY KEY (`group_id`,`username`) USING BTREE,
  KEY `idx_group_id` (`group_id`) USING BTREE,
  KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_host`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_host` (
  `id` varchar(64) NOT NULL,
  `cluster_id` varchar(64) NOT NULL COMMENT '集群编码',
  `ip` varchar(15) NOT NULL COMMENT '主机IP',
  `room` varchar(64) DEFAULT NULL COMMENT '机房',
  `seat` varchar(64) DEFAULT NULL COMMENT '机位',
  `hdd_path` varchar(255) DEFAULT NULL COMMENT 'hdd设备路径',
  `ssd_path` varchar(255) DEFAULT NULL COMMENT 'ssd设备路径',
  `remote_storage_id` varchar(64) DEFAULT NULL COMMENT '外置存储编码',
  `max_usage` int(11) NOT NULL,
  `role` varchar(64) NOT NULL COMMENT '角色',
  `relate_id` varchar(64) DEFAULT NULL COMMENT '关联编码',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `gmt_create` datetime NOT NULL,
  `creator` varchar(32) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ip` (`ip`) USING BTREE,
  KEY `idx_cluster_id` (`cluster_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='主集表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_operate_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_operate_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `obj_type` varchar(64) NOT NULL COMMENT '对象类型',
  `obj_name` varchar(255) NOT NULL COMMENT '对象名',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `site_id` varchar(64) DEFAULT NULL COMMENT '站点编码',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_gmt_create&site_id` (`gmt_create`,`site_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=528 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_order`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_order` (
  `id` varchar(64) NOT NULL COMMENT '工单编码',
  `order_group_id` varchar(64) NOT NULL COMMENT '工单组编码',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `major_version` int(11) unsigned DEFAULT NULL COMMENT '主版本',
  `minor_version` int(11) unsigned DEFAULT NULL COMMENT '次版本',
  `patch_version` int(11) unsigned DEFAULT NULL COMMENT '修订版本',
  `build_version` int(11) unsigned DEFAULT NULL COMMENT '编译版本',
  `arch_mode` varchar(32) DEFAULT NULL COMMENT '架构类型',
  `unit_cnt` int(11) unsigned DEFAULT NULL COMMENT '单元数量',
  `cpu_cnt` double unsigned DEFAULT NULL COMMENT 'CPU数量',
  `mem_size` double unsigned DEFAULT NULL COMMENT '内存容量，单位G',
  `disk_type` varchar(32) DEFAULT NULL COMMENT '磁盘类型',
  `data_size` int(11) unsigned DEFAULT NULL COMMENT '数据目录大小，单位G',
  `log_size` int(11) unsigned DEFAULT NULL COMMENT '日志目录大小，单位G',
  `port` int(11) unsigned DEFAULT NULL COMMENT '端口',
  `cfg` text COMMENT '配置',
  `cnt` int(11) unsigned DEFAULT NULL COMMENT '个数',
  `cluster_ha` tinyint(1) unsigned DEFAULT NULL COMMENT '集群高可用',
  `host_ha` tinyint(1) unsigned DEFAULT NULL COMMENT '主机高可用',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_group_id` (`order_group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='工单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_order_cfg`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_order_cfg` (
  `category` varchar(32) NOT NULL COMMENT '服务种类',
  `type` varchar(32) NOT NULL COMMENT '服务类型',
  `cpu_cnt` double unsigned NOT NULL COMMENT 'CPU数量',
  `mem_size` double unsigned NOT NULL COMMENT '内存容量，单位G',
  `disk_type` varchar(32) NOT NULL COMMENT '磁盘类型',
  `data_size` int(11) unsigned NOT NULL COMMENT '数据目录大小，单位G',
  `log_size` int(11) unsigned NOT NULL COMMENT '日志目录大小，单位G',
  `port` int(11) unsigned NOT NULL COMMENT '端口',
  `cluster_ha` tinyint(1) unsigned NOT NULL COMMENT '集群高可用',
  `host_ha` tinyint(1) unsigned NOT NULL COMMENT '主机高可用',
  PRIMARY KEY (`category`,`type`) USING BTREE,
  UNIQUE KEY `uk_category_type` (`category`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='工单配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_order_group`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_order_group` (
  `id` varchar(64) NOT NULL COMMENT '工单组编码',
  `category` varchar(32) NOT NULL COMMENT '服务种类',
  `business_subsystem_id` varchar(64) NOT NULL COMMENT '业务子系统编码',
  `business_area_id` varchar(64) NOT NULL COMMENT '业务区编码',
  `sys_architecture` varchar(64) NOT NULL COMMENT '系统架构',
  `name` varchar(32) NOT NULL COMMENT '工单组名',
  `create_type` varchar(16) NOT NULL COMMENT '工单类型',
  `state` varchar(16) NOT NULL COMMENT '工单状态',
  `msg` varchar(255) NOT NULL COMMENT '审批信息',
  `owner` varchar(32) NOT NULL COMMENT '所属者',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `editor` varchar(32) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx` (`business_subsystem_id`,`business_area_id`,`category`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='工单组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_privilege`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_privilege` (
  `code` varchar(64) NOT NULL COMMENT '权限代码',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `is_global` tinyint(1) unsigned NOT NULL COMMENT '是否为全局权限',
  `sequence` int(11) unsigned NOT NULL COMMENT '显示顺序',
  PRIMARY KEY (`code`,`is_global`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='数据库权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(16) NOT NULL COMMENT '角色名称',
  `description` varchar(255) NOT NULL COMMENT '角色描述',
  `is_manager` tinyint(1) NOT NULL,
  `data_scope` varchar(255) NOT NULL,
  `is_sys` tinyint(1) unsigned NOT NULL COMMENT '是否为系统资源',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_role_cfg_app`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_role_cfg_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(32) NOT NULL COMMENT '角色id',
  `app_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_scale`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_scale` (
  `type` varchar(32) NOT NULL COMMENT '服务类型',
  `cpu_cnt` double unsigned NOT NULL COMMENT 'cpu数量',
  `mem_size` double unsigned NOT NULL COMMENT '内存容量(G)',
  `name` varchar(16) NOT NULL COMMENT '规模名称',
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `sequence` int(11) unsigned NOT NULL COMMENT '显示顺序',
  PRIMARY KEY (`type`,`cpu_cnt`,`mem_size`) USING BTREE,
  UNIQUE KEY `uk_type&cpu&mem` (`type`,`cpu_cnt`,`mem_size`) USING BTREE,
  UNIQUE KEY `uk_type&name` (`type`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='规模表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_serv`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_serv` (
  `id` varchar(64) NOT NULL,
  `serv_group_id` varchar(64) NOT NULL COMMENT '服务组编码',
  `type` varchar(32) NOT NULL COMMENT '服务类型',
  `major_version` int(11) unsigned NOT NULL COMMENT '主版本',
  `minor_version` int(11) unsigned NOT NULL COMMENT '次版本',
  `patch_version` int(11) unsigned NOT NULL COMMENT '修订版本',
  `build_version` int(11) unsigned NOT NULL COMMENT '编译版本',
  `arch_mode` varchar(32) NOT NULL COMMENT '架构类型',
  `unit_cnt` int(11) unsigned NOT NULL COMMENT '单元数量',
  `cpu_cnt` double unsigned NOT NULL COMMENT 'CPU数量',
  `mem_size` double unsigned NOT NULL COMMENT '内存容量，单位G',
  `disk_type` varchar(32) DEFAULT NULL COMMENT '磁盘类型',
  `data_size` int(11) unsigned NOT NULL COMMENT '数据目录大小，单位G',
  `log_size` int(11) unsigned NOT NULL COMMENT '日志目录大小，单位G',
  `port` int(11) unsigned DEFAULT NULL COMMENT '端口',
  `relate_id` varchar(64) DEFAULT NULL COMMENT '关联编码',
  `monitor_flag` tinyint(1) unsigned DEFAULT NULL COMMENT '监控注册是否成功',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_serv_group_id` (`serv_group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='服务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_serv_group`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_serv_group` (
  `id` varchar(64) NOT NULL COMMENT '服务组编码',
  `category` varchar(32) NOT NULL COMMENT '服务组种类',
  `business_subsystem_id` varchar(64) NOT NULL COMMENT '业务子系统编码',
  `business_area_id` varchar(64) NOT NULL COMMENT '业务区编码',
  `sys_architecture` varchar(64) NOT NULL COMMENT '系统架构',
  `name` varchar(32) NOT NULL COMMENT '服务组名',
  `owner` varchar(32) NOT NULL COMMENT '所属者',
  `flag` tinyint(1) unsigned DEFAULT NULL COMMENT '是否创建成功',
  `order_group_id` varchar(64) NOT NULL COMMENT '关联最新工单组编码',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_name` (`name`) USING BTREE,
  KEY `idx` (`business_subsystem_id`,`business_area_id`,`category`,`owner`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='服务组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_subtask`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_subtask` (
  `id` varchar(64) NOT NULL COMMENT '子任务编码',
  `task_id` varchar(64) NOT NULL COMMENT '任务编码',
  `obj_type` varchar(32) NOT NULL COMMENT '对象类型',
  `obj_id` varchar(64) NOT NULL COMMENT '对象编码',
  `obj_name` varchar(64) NOT NULL COMMENT '对象名称',
  `action_type` varchar(64) NOT NULL COMMENT '动作类型',
  `priority` int(11) unsigned NOT NULL COMMENT '优先级',
  `start_datetime` datetime DEFAULT NULL COMMENT '开始时间',
  `end_datetime` datetime DEFAULT NULL COMMENT '结束时间',
  `state` varchar(32) NOT NULL COMMENT '子任务状态',
  `timeout` int(11) unsigned NOT NULL COMMENT '超时时间，单位秒',
  `msg` text COMMENT '子任务执行信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='子任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_subtask_cfg`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_subtask_cfg` (
  `obj_type` varchar(64) NOT NULL COMMENT '对象类型',
  `action_type` varchar(64) NOT NULL COMMENT '动作类型',
  `timeout` int(10) unsigned NOT NULL COMMENT '超时时间，单位秒',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`obj_type`,`action_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='子任务超时配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_task`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_task` (
  `id` varchar(64) NOT NULL,
  `site_id` varchar(64) NOT NULL COMMENT '站点编码',
  `obj_type` varchar(32) NOT NULL COMMENT '对象类型',
  `obj_id` varchar(64) NOT NULL COMMENT '对象编码',
  `obj_name` varchar(64) NOT NULL COMMENT '对象名',
  `action_type` varchar(32) NOT NULL COMMENT '动作类型',
  `is_block` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否阻塞',
  `start_datetime` datetime DEFAULT NULL COMMENT '开始时间',
  `end_datetime` datetime DEFAULT NULL COMMENT '结束时间',
  `state` varchar(32) NOT NULL DEFAULT '' COMMENT '任务状态',
  `owner` varchar(32) NOT NULL COMMENT '所属者',
  `sequence` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增顺序',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uk_sequence` (`sequence`) USING BTREE,
  KEY `idx` (`site_id`,`start_datetime`,`end_datetime`,`obj_type`,`obj_id`,`owner`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_unit`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_unit` (
  `id` varchar(64) NOT NULL,
  `serv_id` varchar(64) NOT NULL COMMENT '服务编码',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `relate_id` varchar(64) DEFAULT NULL COMMENT '关联编码',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_serv_id` (`serv_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='单元表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_user` (
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `auth_type` varchar(32) NOT NULL COMMENT '认证方式',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `name` varchar(16) NOT NULL COMMENT '姓名',
  `telephone` varchar(128) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL COMMENT '电子邮箱',
  `company` varchar(32) DEFAULT NULL COMMENT '所属单位',
  `emer_contact` varchar(16) DEFAULT NULL COMMENT '紧急联系人',
  `emer_tel` varchar(128) DEFAULT NULL,
  `is_enabled` tinyint(1) unsigned NOT NULL COMMENT '是否可用',
  `role_id` varchar(32) NOT NULL COMMENT '角色编码',
  `og_auto_examine` tinyint(1) unsigned NOT NULL COMMENT '工单自动审批',
  `og_auto_execute` tinyint(1) unsigned NOT NULL COMMENT '工单自动执行',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建者',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `editor` varchar(32) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
