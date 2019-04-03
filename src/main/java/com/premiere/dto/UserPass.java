package com.premiere.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_pass")
@Data
public class UserPass {

    @Id
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "user_id")
    private String userId;

}