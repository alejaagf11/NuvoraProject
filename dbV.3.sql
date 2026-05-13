-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: nuvora_finanzas
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `abono`
--

DROP TABLE IF EXISTS `abono`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `abono` (
  `abono_id` bigint NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `monto` double DEFAULT NULL,
  `meta_ahorro_id` bigint DEFAULT NULL,
  PRIMARY KEY (`abono_id`),
  KEY `FK7ppo7iwq9h74uw7h79a3lhtn4` (`meta_ahorro_id`),
  CONSTRAINT `FK7ppo7iwq9h74uw7h79a3lhtn4` FOREIGN KEY (`meta_ahorro_id`) REFERENCES `metas_ahorro` (`meta_ahorro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `abono`
--

LOCK TABLES `abono` WRITE;
/*!40000 ALTER TABLE `abono` DISABLE KEYS */;
INSERT INTO `abono` VALUES (1,'2026-03-25',500,3),(2,'2026-03-26',4000,5),(3,'2026-04-02',4000,6),(5,'2026-04-07',5000,8),(6,'2026-04-08',500000,9),(7,'2026-05-08',500000,10),(8,'2026-05-09',200000,12);
/*!40000 ALTER TABLE `abono` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `categoria_id` bigint NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(255) NOT NULL,
  `tipo_categoria` varchar(255) NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`categoria_id`),
  KEY `FK6byc7k2m35w3fwmcba5f0rqtf` (`usuario_id`),
  CONSTRAINT `FK6byc7k2m35w3fwmcba5f0rqtf` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Entretenimiento Octubre','GASTO',7),(2,'Entretenimiento Octubre','GASTO',8),(3,'Entretenimiento','GASTO',8);
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leccion`
--

DROP TABLE IF EXISTS `leccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leccion` (
  `leccion_id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `contenido_leccion` text NOT NULL,
  `orden_leccion` int NOT NULL,
  `titulo_leccion` varchar(255) NOT NULL,
  `modulo_id` bigint NOT NULL,
  PRIMARY KEY (`leccion_id`),
  KEY `FKjh6jx5dxxtmlgdcib6r2m93tn` (`modulo_id`),
  CONSTRAINT `FKjh6jx5dxxtmlgdcib6r2m93tn` FOREIGN KEY (`modulo_id`) REFERENCES `modulo_aprendizaje` (`modulo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leccion`
--

LOCK TABLES `leccion` WRITE;
/*!40000 ALTER TABLE `leccion` DISABLE KEYS */;
INSERT INTO `leccion` VALUES (1,_binary '','Los gastos fijos suelen repetirse cada mes, como arriendo o servicios. Los variables cambian, como transporte, ocio o compras extras.',1,'Gastos fijos vs variables',1),(2,_binary '','Pequeños gastos diarios como snacks, domicilios o suscripciones pueden acumularse y afectar tus finanzas sin que lo notes.',2,'Detecta fugas de dinero',1),(3,_binary '','Pequeños gastos diarios como snacks, domicilios o suscripciones pueden acumularse y afectar tus finanzas sin que lo notes.',2,'Detecta fugas de dinero',1);
/*!40000 ALTER TABLE `leccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metas_ahorro`
--

DROP TABLE IF EXISTS `metas_ahorro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metas_ahorro` (
  `meta_ahorro_id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_limite` date NOT NULL,
  `monto_objetivo` double NOT NULL,
  `nombre_meta` varchar(255) NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  `ahorro_mensual` double DEFAULT NULL,
  `monto_ahorrado` double DEFAULT NULL,
  PRIMARY KEY (`meta_ahorro_id`),
  KEY `FKdu022myb12toen32eww2v0fx1` (`usuario_id`),
  CONSTRAINT `FKdu022myb12toen32eww2v0fx1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metas_ahorro`
--

LOCK TABLES `metas_ahorro` WRITE;
/*!40000 ALTER TABLE `metas_ahorro` DISABLE KEYS */;
INSERT INTO `metas_ahorro` VALUES (3,'2026-12-21',26000,'Visa Australia',5,3250,500),(4,'2026-12-21',24000,'Visa Australia',5,3000,0),(5,'2026-12-21',24000,'Carro',6,3000,4000),(6,'2026-10-21',987000,'concierto',7,164500,4000),(8,'2026-10-21',980000,'concierto',9,162500,5000),(9,'2026-10-21',2500000,'televisor',9,285714.29,500000),(10,'2028-05-08',2500000,'televisor',8,80000,500000),(11,'2028-05-08',28000000,'carro',8,1166666.67,0),(12,'2028-05-09',2800000,'patineta',8,108333.33,200000);
/*!40000 ALTER TABLE `metas_ahorro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modulo_aprendizaje`
--

DROP TABLE IF EXISTS `modulo_aprendizaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modulo_aprendizaje` (
  `modulo_id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `descripcion_modulo` varchar(255) NOT NULL,
  `orden_modulo` int NOT NULL,
  `titulo_modulo` varchar(255) NOT NULL,
  PRIMARY KEY (`modulo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modulo_aprendizaje`
--

LOCK TABLES `modulo_aprendizaje` WRITE;
/*!40000 ALTER TABLE `modulo_aprendizaje` DISABLE KEYS */;
INSERT INTO `modulo_aprendizaje` VALUES (1,_binary '','Aprende a identificar en que se va tu dinero y toma el control',1,'Domina tus gastos');
/*!40000 ALTER TABLE `modulo_aprendizaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presupuesto`
--

DROP TABLE IF EXISTS `presupuesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `presupuesto` (
  `presupuesto_id` bigint NOT NULL AUTO_INCREMENT,
  `limite_monto_presu` double NOT NULL,
  `mes_presupuesto` varchar(255) NOT NULL,
  `categoria_id` bigint DEFAULT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`presupuesto_id`),
  KEY `FK3fglw7gm8tjjyxhpeq9s8hu1m` (`categoria_id`),
  KEY `FKl902xsrmvqx3rsyrvu57eyno` (`usuario_id`),
  CONSTRAINT `FK3fglw7gm8tjjyxhpeq9s8hu1m` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`),
  CONSTRAINT `FKl902xsrmvqx3rsyrvu57eyno` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presupuesto`
--

LOCK TABLES `presupuesto` WRITE;
/*!40000 ALTER TABLE `presupuesto` DISABLE KEYS */;
/*!40000 ALTER TABLE `presupuesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progreso_leccion_usuario`
--

DROP TABLE IF EXISTS `progreso_leccion_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progreso_leccion_usuario` (
  `progreso_id` bigint NOT NULL AUTO_INCREMENT,
  `completada` bit(1) NOT NULL,
  `fecha_completada` date DEFAULT NULL,
  `leccion_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`progreso_id`),
  KEY `FKo43j9ykhqqiyyq02dl2rn6fmb` (`leccion_id`),
  KEY `FK5cibr9n2cr8k2umvq8sund8y8` (`usuario_id`),
  CONSTRAINT `FK5cibr9n2cr8k2umvq8sund8y8` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`),
  CONSTRAINT `FKo43j9ykhqqiyyq02dl2rn6fmb` FOREIGN KEY (`leccion_id`) REFERENCES `leccion` (`leccion_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progreso_leccion_usuario`
--

LOCK TABLES `progreso_leccion_usuario` WRITE;
/*!40000 ALTER TABLE `progreso_leccion_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `progreso_leccion_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tips`
--

DROP TABLE IF EXISTS `tips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tips` (
  `tip_id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion_tip` varchar(255) NOT NULL,
  `titulo_tip` varchar(255) NOT NULL,
  PRIMARY KEY (`tip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tips`
--

LOCK TABLES `tips` WRITE;
/*!40000 ALTER TABLE `tips` DISABLE KEYS */;
/*!40000 ALTER TABLE `tips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaccion`
--

DROP TABLE IF EXISTS `transaccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaccion` (
  `transaccion_id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion_transaccion` varchar(255) NOT NULL,
  `fecha_transaccion` date NOT NULL,
  `monto_transaccion` double NOT NULL,
  `tipo_transaccion` varchar(255) NOT NULL,
  `categoria_id` bigint DEFAULT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`transaccion_id`),
  KEY `FKk7db1p3y2mxyhrflylujs3bx7` (`categoria_id`),
  KEY `FKe4k4dkaj7tcpfnup7hkljdj4u` (`usuario_id`),
  CONSTRAINT `FKe4k4dkaj7tcpfnup7hkljdj4u` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`),
  CONSTRAINT `FKk7db1p3y2mxyhrflylujs3bx7` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaccion`
--

LOCK TABLES `transaccion` WRITE;
/*!40000 ALTER TABLE `transaccion` DISABLE KEYS */;
INSERT INTO `transaccion` VALUES (1,'Compra outfit','2026-04-02',155,'GASTO',1,7),(2,'Compra outfit','2026-04-03',155,'GASTO',2,8);
/*!40000 ALTER TABLE `transaccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `usuario_id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_contrasena` varchar(255) NOT NULL,
  `usuario_correo` varchar(255) DEFAULT NULL,
  `usuario_nombre` varchar(255) NOT NULL,
  `usuario_rol` varchar(255) NOT NULL,
  `usuario_monto_mensual` double NOT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `UK76u9293x3ub1irldnugaed7w6` (`usuario_correo`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (3,'$2a$10$CFUw/Z.Ld2LowDvusUGjDOeBRb1yKkZ2KplYlJ3BkXBc4IA0w264O','laura12@gmail.com','Laura','USER',0),(4,'$2a$10$h/CjgVkWciZ5jICyVb62EeD/MKR/JgV1gAZDa.XGY1nB1ZRQXF0fi','vale12@gmail.com','Valentina','USER',0),(5,'$2a$10$V6qPZVKIgwFDxc5Ov0dA1eyf3YhM9aR8wRUEqV8fvNDwSfHGpqNZK','dianitaPineros334@gmail.com','Diana Pineros','USER',0),(6,'$2a$10$A.TB.qD83qNyWqzRNjqnT.cXCtUqD1PSbbMQpBiyJNRsxyTEw5Zqa','leoGomez2@gmail.com','Leonardo Gomez','USER',0),(7,'$2a$10$JX77rwe8M0UGXjA7b8Eue.pRpPQQt4UPfimA3dOlIa.ICT2WMhSiO','lauG12@gmail.com','Laurita Gonzalez','USER',0),(8,'$2a$10$arYgm5xPbSW5BG7rnywBA.E7ewlPw1P9l/i1mTLxqOwDvxE6SAzaG','taeKim@gmail.com','Tae Tae','USER',0),(9,'$2a$10$FHUnF7XHLuCaHu7De4MpyOyCKtDVk92edl9PNysUSFQmas5NQLDde','namHyung@gmail.com','Namu','USER',0),(10,'$2a$10$sMELvrWk3IRVtuqMBaeJhuIcYChboR97lL5Z76bRVeK3PZLBDhjQm','admin@gmail.com','ADMIN','ADMIN',0);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-13  8:17:40
