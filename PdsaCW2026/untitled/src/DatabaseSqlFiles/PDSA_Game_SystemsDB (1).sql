-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: game_system
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `algorithm_times`
--

DROP TABLE IF EXISTS `algorithm_times`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `algorithm_times` (
  `time_id` int NOT NULL AUTO_INCREMENT,
  `round_id` int DEFAULT NULL,
  `algorithm_name` varchar(100) DEFAULT NULL,
  `time_taken` bigint DEFAULT NULL,
  PRIMARY KEY (`time_id`),
  KEY `round_id` (`round_id`),
  CONSTRAINT `algorithm_times_ibfk_1` FOREIGN KEY (`round_id`) REFERENCES `game_rounds` (`round_id`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm_times`
--

LOCK TABLES `algorithm_times` WRITE;
/*!40000 ALTER TABLE `algorithm_times` DISABLE KEYS */;
INSERT INTO `algorithm_times` VALUES (1,1,'BFS',220700),(2,1,'Dijkstra',1664000),(3,2,'BFS',268000),(4,2,'Dijkstra',1660100),(5,3,'BFS',317000),(6,3,'Dijkstra',1586400),(7,4,'BFS',27800),(8,4,'Dijkstra',62300),(9,5,'BFS',221000),(10,5,'Dijkstra',1512500),(11,6,'BFS',228400),(12,6,'Dijkstra',1524300),(13,7,'BFS',232000),(14,7,'Dijkstra',1585100),(15,8,'BFS',23300),(16,8,'Dijkstra',76700),(17,9,'BFS',9400),(18,9,'Dijkstra',24300),(19,10,'BFS',311000),(20,10,'Dijkstra',1770200),(21,11,'BFS',23800),(22,11,'Dijkstra',49500),(23,12,'BFS',12000),(24,12,'Dijkstra',35100),(25,13,'BFS',10300),(26,13,'Dijkstra',32000),(27,14,'BFS',270400),(28,14,'Dijkstra',1532800),(29,15,'BFS',310900),(30,15,'Dijkstra',1901700),(31,16,'BFS',230000),(32,16,'Dijkstra',1725900),(33,17,'BFS',323200),(34,17,'Dijkstra',1798300),(35,18,'BFS',235000),(36,18,'Dijkstra',1534800),(37,19,'BFS',34600),(38,19,'Dijkstra',121100),(39,20,'BFS',28400),(40,20,'Dijkstra',90200),(41,21,'BFS',26700),(42,21,'Dijkstra',52500),(43,22,'BFS',18800),(44,22,'Dijkstra',26300),(45,23,'BFS',25400),(46,23,'Dijkstra',59600),(47,24,'BFS',12400),(48,24,'Dijkstra',34000),(49,25,'BFS',9500),(50,25,'Dijkstra',19800),(51,26,'BFS',39000),(52,26,'Dijkstra',72400),(53,27,'BFS',14300),(54,27,'Dijkstra',39200),(55,28,'Greedy',472400),(56,28,'Hungarian',1698400),(57,29,'Greedy',102100),(58,29,'Hungarian',682600),(59,30,'Greedy',272300),(60,30,'Hungarian',1199100),(61,31,'EdmondsKarp',310400),(62,31,'FordFulkerson',230700),(63,32,'EdmondsKarp',51400),(64,32,'FordFulkerson',41200),(65,33,'EdmondsKarp',50500),(66,33,'FordFulkerson',44200),(67,34,'EdmondsKarp',61700),(68,34,'FordFulkerson',69600),(69,35,'EdmondsKarp',46400),(70,35,'FordFulkerson',32900),(71,36,'EdmondsKarp',51100),(72,36,'FordFulkerson',40700),(73,37,'EdmondsKarp',72400),(74,37,'FordFulkerson',47900),(75,38,'EdmondsKarp',77000),(76,38,'FordFulkerson',32000),(77,39,'EdmondsKarp',43500),(78,39,'FordFulkerson',89800),(79,40,'EdmondsKarp',38600),(80,40,'FordFulkerson',20300),(81,41,'EdmondsKarp',36300),(82,41,'FordFulkerson',17100),(83,42,'EdmondsKarp',37100),(84,42,'FordFulkerson',18400),(85,43,'EdmondsKarp',303100),(86,43,'FordFulkerson',274300),(87,44,'EdmondsKarp',44700),(88,44,'FordFulkerson',36300),(89,45,'EdmondsKarp',51500),(90,45,'FordFulkerson',34900),(91,46,'EdmondsKarp',53200),(92,46,'FordFulkerson',49400),(93,47,'EdmondsKarp',48100),(94,47,'FordFulkerson',43000),(95,48,'EdmondsKarp',44700),(96,48,'FordFulkerson',36200),(97,49,'EdmondsKarp',37300),(98,49,'FordFulkerson',35800),(99,50,'EdmondsKarp',88200),(100,50,'FordFulkerson',45900),(101,51,'EdmondsKarp',358500),(102,51,'FordFulkerson',243800),(103,52,'EdmondsKarp',51300),(104,52,'FordFulkerson',46200),(105,59,'Heuristic',462000),(106,60,'Heuristic',696400),(107,60,'Heuristic',40600),(108,61,'Heuristic',446400),(109,62,'Heuristic',411400),(110,62,'Heuristic',44100),(111,63,'Heuristic',414000),(112,65,'Heuristic',376500),(113,65,'Heuristic',49500),(114,65,'Heuristic',42500),(115,66,'Greedy',217200),(116,66,'Hungarian',1038300),(117,67,'Greedy',135600),(118,67,'Hungarian',910700),(119,68,'Greedy',298700),(120,68,'Hungarian',1343000),(121,69,'Greedy',137600),(122,69,'Hungarian',1110100),(123,70,'Greedy',110000),(124,70,'Hungarian',618900),(125,71,'Greedy',353300),(126,71,'Hungarian',1111000),(127,72,'BFS',254000),(128,72,'Dijkstra',1779700),(129,73,'Greedy',369200),(130,73,'Hungarian',1341900),(131,74,'BFS',263700),(132,74,'Dijkstra',1888000),(133,75,'Greedy',65900),(134,75,'Hungarian',465800),(135,76,'EdmondsKarp',333100),(136,76,'FordFulkerson',317200),(137,78,'BFS',16000),(138,78,'Dijkstra',36200),(139,79,'Greedy',60300),(140,79,'Hungarian',346200),(141,80,'BFS',260200),(142,80,'Dijkstra',1987700),(143,81,'BFS',311200),(144,81,'Dijkstra',1792200),(145,82,'BFS',13800),(146,82,'Dijkstra',35100),(147,83,'BFS',17900),(148,83,'Dijkstra',28100),(149,84,'BFS',24000),(150,84,'Dijkstra',30900),(151,85,'BFS',44900),(152,85,'Dijkstra',89300),(153,86,'Greedy',309700),(154,86,'Hungarian',1874700),(155,87,'Greedy',296100),(156,87,'Hungarian',1768400),(157,88,'Heuristic',450000),(158,88,'Heuristic',46500),(159,88,'Heuristic',183900),(160,88,'Heuristic',53400),(161,88,'Heuristic',35200),(162,89,'Heuristic',464000),(163,89,'Heuristic',46000),(164,90,'EdmondsKarp',293300),(165,90,'FordFulkerson',252700),(166,91,'EdmondsKarp',457200),(167,91,'FordFulkerson',270300),(168,92,'EdmondsKarp',43400),(169,92,'FordFulkerson',35900),(170,93,'EdmondsKarp',51900),(171,93,'FordFulkerson',40300),(172,94,'Greedy',326800),(173,94,'Hungarian',1974300),(174,95,'BFS',246800),(175,95,'Dijkstra',2106700),(176,96,'Greedy',168900),(177,96,'Hungarian',1116900),(178,97,'EdmondsKarp',49200),(179,97,'FordFulkerson',54200);
/*!40000 ALTER TABLE `algorithm_times` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_rounds`
--

DROP TABLE IF EXISTS `game_rounds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_rounds` (
  `round_id` int NOT NULL AUTO_INCREMENT,
  `game_type` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`round_id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_rounds`
--

LOCK TABLES `game_rounds` WRITE;
/*!40000 ALTER TABLE `game_rounds` DISABLE KEYS */;
INSERT INTO `game_rounds` VALUES (1,'Snake','2026-04-18 07:55:11'),(2,'Snake','2026-04-18 07:56:30'),(3,'Snake','2026-04-18 07:57:06'),(4,'Snake','2026-04-18 07:57:46'),(5,'Snake','2026-04-18 08:04:34'),(6,'Snake','2026-04-18 08:09:57'),(7,'Snake','2026-04-18 08:11:01'),(8,'Snake','2026-04-18 08:11:21'),(9,'Snake','2026-04-18 08:11:29'),(10,'Snake','2026-04-18 08:15:20'),(11,'Snake','2026-04-18 08:15:52'),(12,'Snake','2026-04-18 08:16:57'),(13,'Snake','2026-04-18 08:17:03'),(14,'Snake','2026-04-18 08:23:50'),(15,'Snake','2026-04-18 08:30:54'),(16,'Snake','2026-04-18 08:31:31'),(17,'Snake','2026-04-18 08:32:34'),(18,'Snake','2026-04-18 08:33:09'),(19,'Snake','2026-04-18 08:33:16'),(20,'Snake','2026-04-18 08:33:19'),(21,'Snake','2026-04-18 08:33:21'),(22,'Snake','2026-04-18 08:33:22'),(23,'Snake','2026-04-18 08:33:24'),(24,'Snake','2026-04-18 08:33:24'),(25,'Snake','2026-04-18 08:33:25'),(26,'Snake','2026-04-18 08:33:26'),(27,'Snake','2026-04-18 08:33:26'),(28,'MinimumCost','2026-04-18 08:50:47'),(29,'MinimumCost','2026-04-18 08:51:02'),(30,'MinimumCost','2026-04-18 08:55:35'),(31,'Traffic','2026-04-18 09:24:12'),(32,'Traffic','2026-04-18 09:24:28'),(33,'Traffic','2026-04-18 09:24:30'),(34,'Traffic','2026-04-18 09:24:31'),(35,'Traffic','2026-04-18 09:24:44'),(36,'Traffic','2026-04-18 09:24:59'),(37,'Traffic','2026-04-18 09:25:01'),(38,'Traffic','2026-04-18 09:25:01'),(39,'Traffic','2026-04-18 09:25:02'),(40,'Traffic','2026-04-18 09:25:03'),(41,'Traffic','2026-04-18 09:25:03'),(42,'Traffic','2026-04-18 09:25:03'),(43,'Traffic','2026-04-18 09:28:40'),(44,'Traffic','2026-04-18 09:30:12'),(45,'Traffic','2026-04-18 09:30:13'),(46,'Traffic','2026-04-18 09:30:13'),(47,'Traffic','2026-04-18 09:30:14'),(48,'Traffic','2026-04-18 09:30:16'),(49,'Traffic','2026-04-18 09:30:16'),(50,'Traffic','2026-04-18 09:30:16'),(51,'Traffic','2026-04-18 09:31:53'),(52,'Traffic','2026-04-18 09:31:57'),(53,'KnightTour','2026-04-18 09:48:23'),(54,'KnightTour','2026-04-18 09:51:48'),(55,'KnightTour','2026-04-18 09:52:02'),(56,'KnightTour','2026-04-18 09:54:37'),(57,'KnightTour','2026-04-18 09:54:44'),(58,'KnightTour','2026-04-18 09:56:11'),(59,'KnightTour','2026-04-18 10:00:22'),(60,'KnightTour','2026-04-18 10:02:17'),(61,'KnightTour','2026-04-18 10:05:11'),(62,'KnightTour','2026-04-18 10:06:35'),(63,'KnightTour','2026-04-18 10:09:03'),(64,'KnightTour','2026-04-18 10:11:24'),(65,'KnightTour','2026-04-18 10:13:51'),(66,'MinimumCost','2026-04-18 10:42:30'),(67,'MinimumCost','2026-04-18 10:42:36'),(68,'MinimumCost','2026-04-18 10:53:04'),(69,'MinimumCost','2026-04-18 10:53:09'),(70,'MinimumCost','2026-04-18 10:53:12'),(71,'MinimumCost','2026-04-18 10:59:27'),(72,'Snake','2026-04-18 11:02:28'),(73,'MinimumCost','2026-04-18 11:05:31'),(74,'Snake','2026-04-18 11:05:48'),(75,'MinimumCost','2026-04-18 11:05:51'),(76,'Traffic','2026-04-18 11:05:55'),(77,'KnightTour','2026-04-18 11:06:20'),(78,'Snake','2026-04-18 11:06:22'),(79,'MinimumCost','2026-04-18 11:06:25'),(80,'Snake','2026-04-18 11:08:00'),(81,'Snake','2026-04-18 11:10:07'),(82,'Snake','2026-04-18 11:10:19'),(83,'Snake','2026-04-18 11:10:22'),(84,'Snake','2026-04-18 11:10:23'),(85,'Snake','2026-04-18 11:10:27'),(86,'MinimumCost','2026-04-18 11:16:49'),(87,'MinimumCost','2026-04-18 11:18:25'),(88,'KnightTour','2026-04-18 11:20:12'),(89,'KnightTour','2026-04-18 11:24:06'),(90,'Traffic','2026-04-18 11:24:58'),(91,'Traffic','2026-04-18 11:26:24'),(92,'Traffic','2026-04-18 11:26:57'),(93,'Traffic','2026-04-18 11:27:06'),(94,'MinimumCost','2026-04-18 11:27:13'),(95,'Snake','2026-04-18 11:27:16'),(96,'MinimumCost','2026-04-18 11:27:18'),(97,'Traffic','2026-04-18 11:27:20'),(98,'KnightTour','2026-04-18 11:27:22');
/*!40000 ALTER TABLE `game_rounds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_answers`
--

DROP TABLE IF EXISTS `player_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_answers` (
  `answer_id` int NOT NULL AUTO_INCREMENT,
  `player_id` int DEFAULT NULL,
  `round_id` int DEFAULT NULL,
  `answer` int DEFAULT NULL,
  `is_correct` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`answer_id`),
  KEY `player_id` (`player_id`),
  KEY `round_id` (`round_id`),
  CONSTRAINT `player_answers_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`),
  CONSTRAINT `player_answers_ibfk_2` FOREIGN KEY (`round_id`) REFERENCES `game_rounds` (`round_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_answers`
--

LOCK TABLES `player_answers` WRITE;
/*!40000 ALTER TABLE `player_answers` DISABLE KEYS */;
INSERT INTO `player_answers` VALUES (1,1,3,12,0),(2,1,3,15,0),(3,2,7,5,0),(4,2,8,6,0),(5,3,10,11,1),(6,3,11,9,1),(7,3,12,2,0),(8,1,17,7,1),(9,4,18,5,0),(10,4,19,12,0),(11,4,20,7,1),(12,4,21,6,1),(13,4,22,1,0),(14,4,23,6,1),(15,4,24,7,0),(16,4,25,1,0),(17,4,26,6,1),(18,1,28,354,0),(19,1,31,24,0),(20,1,32,17,0),(21,1,33,19,0),(22,1,34,13,1),(23,1,35,19,1),(24,1,36,25,1),(25,1,37,19,0),(26,1,38,24,0),(27,1,39,23,0),(28,1,40,18,1),(29,1,41,16,0),(30,1,43,15,0),(31,1,52,20,0),(32,1,59,1,1),(33,1,60,1,1),(34,1,60,1,1),(35,5,61,1,1),(36,6,62,1,1),(37,6,62,1,1),(38,1,63,1,1),(39,7,65,1,1),(40,7,65,1,1),(41,7,65,1,1),(42,1,66,1652,0),(43,1,68,377,1),(44,1,69,494,0),(45,1,76,25,0),(46,8,81,8,0),(47,8,82,6,0),(48,8,83,5,0),(49,8,84,5,1),(50,1,88,1,1),(51,1,88,1,1),(52,1,88,1,1),(53,1,88,1,1),(54,1,88,1,1),(55,7,89,1,1),(56,7,89,1,1),(57,9,91,45,0),(58,9,91,16,1),(59,9,92,16,0);
/*!40000 ALTER TABLE `player_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'Dilshan'),(2,'Shehan Oerera'),(3,'Dilshan Gunawardena'),(4,'Sajith'),(5,'Shehan'),(6,'Tharindu'),(7,'Anura'),(8,'Dislahan'),(9,'Senura');
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solutions`
--

DROP TABLE IF EXISTS `solutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solutions` (
  `solution_id` int NOT NULL AUTO_INCREMENT,
  `round_id` int DEFAULT NULL,
  `correct_answer` int DEFAULT NULL,
  PRIMARY KEY (`solution_id`),
  KEY `round_id` (`round_id`),
  CONSTRAINT `solutions_ibfk_1` FOREIGN KEY (`round_id`) REFERENCES `game_rounds` (`round_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solutions`
--

LOCK TABLES `solutions` WRITE;
/*!40000 ALTER TABLE `solutions` DISABLE KEYS */;
INSERT INTO `solutions` VALUES (1,1,3),(2,2,3),(3,3,5),(4,4,9),(5,5,3),(6,6,6),(7,7,4),(8,8,4),(9,9,3),(10,10,11),(11,11,9),(12,12,4),(13,13,3),(14,14,5),(15,15,6),(16,16,6),(17,17,7),(18,18,4),(19,19,10),(20,20,7),(21,21,6),(22,22,3),(23,23,6),(24,24,5),(25,25,3),(26,26,6),(27,27,3),(28,28,351),(29,29,226),(30,30,304),(31,31,22),(32,32,22),(33,33,20),(34,34,13),(35,35,19),(36,36,25),(37,37,17),(38,38,20),(39,39,19),(40,40,18),(41,41,17),(42,42,17),(43,43,20),(44,44,19),(45,45,17),(46,46,24),(47,47,18),(48,48,19),(49,49,13),(50,50,24),(51,51,13),(52,52,16),(53,66,394),(54,67,397),(55,68,377),(56,69,510),(57,70,413),(58,71,343),(59,72,4),(60,73,450),(61,74,8),(62,75,551),(63,76,16),(64,78,3),(65,79,390),(66,80,8),(67,81,7),(68,82,5),(69,83,3),(70,84,5),(71,85,7),(72,86,364),(73,87,199),(74,90,19),(75,91,16),(76,92,20),(77,93,20),(78,94,425),(79,95,4),(80,96,394),(81,97,17);
/*!40000 ALTER TABLE `solutions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unique_solutions`
--

DROP TABLE IF EXISTS `unique_solutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `unique_solutions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `game_type` varchar(50) DEFAULT NULL,
  `solution_text` text,
  `is_found` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unique_solutions`
--

LOCK TABLES `unique_solutions` WRITE;
/*!40000 ALTER TABLE `unique_solutions` DISABLE KEYS */;
/*!40000 ALTER TABLE `unique_solutions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-18 17:00:37
