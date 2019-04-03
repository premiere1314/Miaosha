package com.premiere.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="user_info")
@Data
public class UserInfo {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "telPhone")
    private String telPhone;

    @Column(name = "register_mode")
    private String registerMode;

    @Column(name = "third_pay_id")
    private String thirdPayId;

}