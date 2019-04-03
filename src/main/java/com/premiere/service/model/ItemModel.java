package com.premiere.service.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ItemModel {

    private String id;

    @NotBlank(message = "商品不能为空")
    private String title;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品的价格必须大于零")
    private BigDecimal price;

    @NotNull(message = "库存不能不填")
    @Min(value = 0,message = "库存不能为零")
    private Integer stock;

    @NotBlank(message = "商品描述不能为空")
    private String description;

    private Integer sales;

    @NotBlank(message = "商品的描述图片信息不能为空")
    private String imgUrl;
}
