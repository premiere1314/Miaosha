package com.premiere.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "promo")
@Data
public class Promo {
    @Id
    private String id;

    @Column(name = "promo_name")
    private String promoName;

    @Column(name = "Promo_item_price")
    private Double promoItemPrice;

    @Column(name = "Item_id")
    private String itemId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_date")
    private Date endDate;
}