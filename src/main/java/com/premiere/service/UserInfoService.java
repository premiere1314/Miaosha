package com.premiere.service;


import com.premiere.service.model.UserInfoModel;
import com.premiere.utils.error.BusinessException;

import java.security.NoSuchAlgorithmException;

public interface UserInfoService {

    UserInfoModel getUserInfo(String id);

    boolean checkUserName(String name);

    void insertUser(UserInfoModel userInfoModel) throws BusinessException, NoSuchAlgorithmException;

    boolean checkTelPhone(String telPhone);

    UserInfoModel getLoginUserInfoModel(String name, String password) throws BusinessException;
}
