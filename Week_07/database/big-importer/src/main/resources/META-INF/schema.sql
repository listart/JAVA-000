-- MySQL Script generated by MySQL Workbench
-- Tue Nov 24 14:58:33 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ecorder
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ec` ;

-- -----------------------------------------------------
-- Schema ec
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ec` DEFAULT CHARACTER SET utf8 ;
USE `ec` ;

-- -----------------------------------------------------
-- Table `ec`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ec`.`user` ;

CREATE TABLE IF NOT EXISTS `ec`.`user` (
  `id` BIGINT(18) NOT NULL AUTO_INCREMENT,
  `sn` CHAR(32) NOT NULL DEFAULT '',
  `nickname` VARCHAR(120) NOT NULL DEFAULT '',
  `integral` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `used_integral` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ec`.`order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ec`.`order` ;

CREATE TABLE IF NOT EXISTS `ec`.`order` (
  `id` BIGINT(18) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sn` CHAR(32) NOT NULL DEFAULT '' COMMENT '用于跟外部系统对接的唯一标识',
  `status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `pay_status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `shipping_status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `user_id` BIGINT(18) NOT NULL,
  `consignee` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '收件人',
  `address` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '收件地址',
  `mobile` VARCHAR(45) NOT NULL DEFAULT '',
  `pay_id` BIGINT(18) NOT NULL DEFAULT 0,
  `pay_name` VARCHAR(120) NOT NULL DEFAULT '',
  `pay_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `shipping_id` BIGINT(18) NOT NULL DEFAULT 0,
  `shipping_name` VARCHAR(120) NOT NULL DEFAULT '',
  `shipping_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `product_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `paid_money` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `add_time` INT(10) NOT NULL DEFAULT 0,
  `confirm_time` INT(10) NOT NULL DEFAULT 0,
  `pay_time` INT(10) NOT NULL DEFAULT 0,
  `shipping_time` INT(10) NOT NULL DEFAULT 0,
  `discount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '折扣率',
  `integral` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `integral_money` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ec`.`item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ec`.`item` ;

CREATE TABLE IF NOT EXISTS `ec`.`item` (
  `id` BIGINT(18) NOT NULL,
  `sn` CHAR(32) NOT NULL DEFAULT '',
  `name` VARCHAR(120) NOT NULL DEFAULT '',
  `supplier_id` BIGINT(18) NOT NULL DEFAULT 0,
  `supplier_name` VARCHAR(120) NOT NULL DEFAULT '',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `is_new` TINYINT(1) NOT NULL DEFAULT 1,
  `discount` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ec`.`sku`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ec`.`sku` ;

CREATE TABLE IF NOT EXISTS `ec`.`sku` (
  `id` BIGINT(18) NOT NULL,
  `sn` CHAR(32) NOT NULL DEFAULT '',
  `item_id` BIGINT(18) NOT NULL DEFAULT 0,
  `name` VARCHAR(120) NOT NULL DEFAULT '',
  `attr_names` VARCHAR(2048) NOT NULL DEFAULT '{}',
  `attr_values` VARCHAR(2048) NOT NULL DEFAULT '{}',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `is_unrestrict` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否不限制库存',
  `stock` INT NOT NULL DEFAULT 0,
  `lock_mount` INT NOT NULL COMMENT '锁定数量',
  `is_new` TINYINT(1) NOT NULL DEFAULT 0,
  `discount` DECIMAL(10,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ec`.`sub_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ec`.`sub_order` ;

CREATE TABLE IF NOT EXISTS `ec`.`sub_order` (
  `id` BIGINT(18) NOT NULL DEFAULT 0,
  `sn` CHAR(32) NOT NULL DEFAULT '',
  `order_id` BIGINT(18) NOT NULL DEFAULT 0,
  `sku_id` BIGINT(18) NOT NULL DEFAULT 0,
  `name` VARCHAR(120) NOT NULL DEFAULT '',
  `attr_names` VARCHAR(2048) NOT NULL DEFAULT '{}',
  `attr_values` VARCHAR(2048) NOT NULL DEFAULT '{}',
  `mount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `pay_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ec`.`order_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ec`.`order_detail` ;

CREATE TABLE IF NOT EXISTS `ec`.`order_detail` (
  `id` BIGINT(18) NOT NULL DEFAULT 0,
  `sn` CHAR(32) NOT NULL DEFAULT '',
  `order_id` BIGINT(18) NOT NULL DEFAULT 0,
  `sub_order_id` BIGINT(18) NOT NULL DEFAULT 0,
  `item_id` BIGINT(18) NOT NULL DEFAULT 0,
  `sku_id` BIGINT(18) NOT NULL DEFAULT 0,
  `item_name` VARCHAR(120) NOT NULL DEFAULT '',
  `sku_name` VARCHAR(120) NOT NULL DEFAULT '',
  `attr_names` VARCHAR(2048) NOT NULL DEFAULT '{}',
  `attr_values` VARCHAR(2048) NOT NULL DEFAULT '{}',
  `mount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `pay_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
