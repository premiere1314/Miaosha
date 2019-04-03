package com.premiere.dto;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
@Data
public class Item {

    @Id
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "sales")
    private Integer sales;

    @Column(name = "imgUrl")
    private String imgUrl;

}