package com.premiere.controller.vo;

import lombok.Data;

@Data
public class OrderInfoVo {

    private String userId;

    private String itemId;

    private Integer amount;
}
