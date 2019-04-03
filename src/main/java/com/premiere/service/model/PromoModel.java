package com.premiere.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
public class PromoModel {

    private String id;

    private String promoName;

    private DateTime startDate;

    private DateTime endDate;

    private String itemId;

    private BigDecimal promoItemPrice;

    private Integer status;
}
