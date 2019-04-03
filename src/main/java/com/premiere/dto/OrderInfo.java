package com.premiere.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "order_info")
@Data
public class OrderInfo {

    @Id
    private String id;

    @Column(name = "user_Id")
    private String userId;

    @Column(name = "item_Id")
    private String itemId;

    @Column(name = "item_Price")
    private Double itemPrice;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "order_Price")
    private BigDecimal orderPrice;

}