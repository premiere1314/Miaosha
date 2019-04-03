package com.premiere.service.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class UserInfoModel {
    private String id;

    @NotBlank(message = "用户名不能为空")
    private String name;

    @Min(value = 0,message = "年龄必须大于0")
    @Max(value = 200,message = "年龄必须小于200")
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    private String telPhone;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String registerMode;

    private String thirdPayId;

}
