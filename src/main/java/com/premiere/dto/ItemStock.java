package com.premiere.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item_stock")
@Data
public class ItemStock {
    @Id
    private String id;

    @Column(name = "stock")
    private Integer stock;
    @Column(name = "item_id")
    private String itemId;

}