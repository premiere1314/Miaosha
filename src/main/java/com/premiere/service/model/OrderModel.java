package com.premiere.service.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderModel {

    //时间戳+随机数+分库分表的字段
    public String id;
    //购买的用户
    @NotBlank(message = "用户未登录或登录已过期")
    private String userId;
    //商品的id
    @NotBlank(message = "商品不能为空")
    private String itemId;
    //商品的单价
    private BigDecimal itemPrice;
    //购买的数量
    @NotNull(message = "数量不能为空")
    @Max(value = 99,message = "数量不能为大于99")
    @Min(value = 0,message = "数量不能为0")
    private Integer amount;
    //购买的金额
    private BigDecimal orderPrice;

    //活动ID
    private String promoId;

}
