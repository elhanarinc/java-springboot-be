CREATE DATABASE IF NOT EXISTS item_db;
use item_db;

CREATE TABLE IF NOT EXISTS `item` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(180) NOT NULL,
    `category` varchar(180) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `picture_url` varchar(255) DEFAULT NULL,
    `price` int(20) unsigned NOT NULL,
    `ts_create` int(11) unsigned NOT NULL,
    `ts_update` int(11) unsigned DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_ts_create` (`ts_create` DESC),
    INDEX `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;