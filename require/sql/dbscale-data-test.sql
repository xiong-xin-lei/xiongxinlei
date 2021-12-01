-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: dbscale_dev
-- ------------------------------------------------------
-- Server version	5.7.33-log

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
-- Dumping data for table `tbl_business_area`
--

LOCK TABLES `tbl_business_area` WRITE;
/*!40000 ALTER TABLE `tbl_business_area` DISABLE KEYS */;
INSERT INTO `tbl_business_area` VALUES ('b4ef400d70b24109a54869137122f6bb','测试业务区一区',1,'','site_test','2020-01-01 00:00:00','root');
/*!40000 ALTER TABLE `tbl_business_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tbl_business_subsystem`
--

LOCK TABLES `tbl_business_subsystem` WRITE;
/*!40000 ALTER TABLE `tbl_business_subsystem` DISABLE KEYS */;
INSERT INTO `tbl_business_subsystem` VALUES ('business_subsystem_default_root','测试子系统',1,'','business_system_default_root','2020-01-01 00:00:00');
/*!40000 ALTER TABLE `tbl_business_subsystem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tbl_business_system`
--

LOCK TABLES `tbl_business_system` WRITE;
/*!40000 ALTER TABLE `tbl_business_system` DISABLE KEYS */;
INSERT INTO `tbl_business_system` VALUES ('business_system_default_root','测试系统',1,'','2020-01-01 00:00:00','root');
/*!40000 ALTER TABLE `tbl_business_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tbl_order_cfg`
--

LOCK TABLES `tbl_order_cfg` WRITE;
/*!40000 ALTER TABLE `tbl_order_cfg` DISABLE KEYS */;
INSERT INTO `tbl_order_cfg` VALUES ('mysql','cmha',0.5,1,'local_hdd',5,5,9600,1,1),('mysql','mysql',1,2,'local_hdd',10,10,3306,1,1),('mysql','proxysql',1,1,'local_hdd',5,5,6033,1,1),('redis','redis',1,1,'local_hdd',10,10,6379,1,1),('redis','redis-sentinel',0.5,0.5,'local_hdd',5,5,26379,1,1);
/*!40000 ALTER TABLE `tbl_order_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tbl_scale`
--

LOCK TABLES `tbl_scale` WRITE;
/*!40000 ALTER TABLE `tbl_scale` DISABLE KEYS */;
INSERT INTO `tbl_scale` VALUES ('cmha',0.5,1,'0.5核1G',1,501000),('mysql',1,2,'1核2G',1,1002000),('mysql',2,4,'2核4G',1,2004000),('proxysql',1,1,'1核1G',1,1001000),('redis',1,1,'1核1G',1,1001000),('redis-sentinel',0.5,0.5,'0.5核0.5G',1,50500);
/*!40000 ALTER TABLE `tbl_scale` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-21 15:04:41
