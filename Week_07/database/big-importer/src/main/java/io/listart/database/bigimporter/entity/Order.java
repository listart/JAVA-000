package io.listart.database.bigimporter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    //`id` BIGINT(18) UNSIGNED NOT NULL AUTO_INCREMENT,
    private Long id;
    //`sn` CHAR(20) NOT NULL DEFAULT '' COMMENT '用于跟外部系统对接的唯一标识',
    private String sn = "";
    //`status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    private Byte status = 0;
    //`pay_status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    private Byte payStatus = 0;
    //`shipping_status` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    private Byte shippingStatus = 0;
    //`user_id` BIGINT(18) NOT NULL,
    private Long userId = 0L;
    //`consignee` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '收件人',
    private String consignee = "";
    //`address` VARCHAR(255) GENERATED ALWAYS AS (''),
    private String address = "";
    //`mobile` VARCHAR(45) NOT NULL DEFAULT '',
    private String mobile = "";
    //`pay_id` BIGINT(18) NOT NULL DEFAULT 0,
    private Long payId = 0L;
    //`pay_name` VARCHAR(120) NOT NULL DEFAULT '',
    private String payName = "";
    //`pay_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double payFee = 0.0;
    //`shipping_id` BIGINT(18) NOT NULL DEFAULT 0,
    private Long shippingId = 0L;
    //`shipping_name` VARCHAR(120) NOT NULL DEFAULT '',
    private String shippingName = "";
    //`shipping_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double shippingFee = 0.0;
    //`product_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double productAmount = 0.0;
    //`paid_money` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double paidMoney = 0.0;
    //`amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double amount = 0.0;
    //`add_time` INT(10) NOT NULL DEFAULT 0,
    private Date addTime = null;
    //`confirm_time` INT(10) NOT NULL DEFAULT 0,
    private Date confirmTime = null;
    //`pay_time` INT(10) NOT NULL DEFAULT 0,
    private Date payTime = null;
    //`shipping_time` INT(10) NOT NULL DEFAULT 0,
    private Date shippingTime = null;
    //`discount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '折扣率',
    private Double discount = 0.0;
    //`integral` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double integral = 0.0;
    //`integral_money` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    private Double integralMoney = 0.0;
}
