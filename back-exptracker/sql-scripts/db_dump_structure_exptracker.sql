CREATE DATABASE IF NOT EXISTS `exptracker_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `exptracker_db`;

-- Table structure for table `campaign`
DROP TABLE IF EXISTS `campaign`;
CREATE TABLE `campaign` (
  `created_at` datetime(6) DEFAULT NULL,
  `creator_user_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` binary(16) NOT NULL,
  `description` text COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_uuid` (`uuid`),
  KEY `FK_creator_user_id` (`creator_user_id`),
  CONSTRAINT `FK_creator_user_id` FOREIGN KEY (`creator_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `campaign_joined_users`
DROP TABLE IF EXISTS `campaign_joined_users`;
CREATE TABLE `campaign_joined_users` (
  `campaign_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK_user_id` (`user_id`),
  KEY `FK_campaign_id` (`campaign_id`),
  CONSTRAINT `FK_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `class`
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` binary(16) NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_uuid` (`uuid`),
  UNIQUE KEY `UK_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `invite_code`
DROP TABLE IF EXISTS `invite_code`;
CREATE TABLE `invite_code` (
  `campaign_id` bigint DEFAULT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_code` (`code`),
  KEY `FK_invite_campaign_id` (`campaign_id`),
  KEY `expiry_date_index` (`expiry_date`),
  CONSTRAINT `FK_invite_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `player_character`
DROP TABLE IF EXISTS `player_character`;
CREATE TABLE `player_character` (
  `active` bit(1) NOT NULL,
  `experience_points` int NOT NULL,
  `campaign_id` bigint DEFAULT NULL,
  `class_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `race_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `character_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_uuid` (`uuid`),
  KEY `FK_pc_campaign_id` (`campaign_id`),
  KEY `FK_pc_user_id` (`user_id`),
  KEY `FK_class_id` (`class_id`),
  KEY `FK_race_id` (`race_id`),
  CONSTRAINT `FK_pc_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_pc_campaign_id` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`),
  CONSTRAINT `FK_class_id` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`),
  CONSTRAINT `FK_race_id` FOREIGN KEY (`race_id`) REFERENCES `race` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `race`
DROP TABLE IF EXISTS `race`;
CREATE TABLE `race` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` binary(16) NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_uuid` (`uuid`),
  UNIQUE KEY `UK_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `user`
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deactivation_expiration_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` binary(16) DEFAULT NULL,
  `password` char(80) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('ROLE_USER','ROLE_ADMIN') COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email` (`email`),
  UNIQUE KEY `UK_uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
