package com.premiere.controller.vo;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
public class ItemVo {

    private String id;
    private String title;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer sales;
    private String imgUrl;


    private String startDate;
    private String endDate;
    private String promoName;
    private BigDecimal promoItemPrice;
    private Integer status;
    private String promoId;
}
