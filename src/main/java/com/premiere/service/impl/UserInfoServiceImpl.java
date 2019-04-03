package com.premiere.service.impl;

import com.premiere.dto.UserInfo;
import com.premiere.dto.UserPass;
import com.premiere.mapper.UserInfoMapper;
import com.premiere.mapper.UserPassMapper;
import com.premiere.service.UserInfoService;
import com.premiere.service.model.UserInfoModel;
import com.premiere.utils.MSUtils;
import com.premiere.utils.validator.ValidatorImpl;
import com.premiere.utils.validator.ValidatorResult;
import com.premiere.utils.error.BusinessException;
import com.premiere.utils.error.EmBusinessError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional()
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final UserPassMapper userPassMapper;
    private final ValidatorImpl validator;

    @Autowired
    public UserInfoServiceImpl(UserPassMapper userPassMapper, UserInfoMapper userInfoMapper, ValidatorImpl validator) {
        this.userPassMapper = userPassMapper;
        this.userInfoMapper = userInfoMapper;
        this.validator = validator;
    }

    /**
     * 测试用
     *
     * @param id 用户ID
     * @return 用户模型
     */
    @Override
    public UserInfoModel getUserInfo(String id) {
        UserInfo userInfo = userInfoMapper.findUserInfoById(id);
        UserInfoModel userInfoModel = this.convertUserInfoModelFormUserInfo(userInfo);
        UserPass userPass = userPassMapper.findUserPassByUserId(id);
        if (null != userPass) {
            userInfoModel.setPassword(userPass.getPassword());
        }
        return userInfoModel;
    }

    /**
     * 检查用户名是否重复
     *
     * @param name 用户名
     * @return 是否重复
     */
    @Override
    public boolean checkUserName(String name) {
        return null == userInfoMapper.findUserInfoByName(name);
    }

    /**
     * 添加用户
     *
     * @param userInfoModel 用户模型
     * @throws BusinessException        异常
     * @throws NoSuchAlgorithmException 异常
     */
    @Override
    public void insertUser(UserInfoModel userInfoModel) throws BusinessException, NoSuchAlgorithmException {
        ValidatorResult result = this.validator.validatorResult(userInfoModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.UN_ERROR, result.getErrorMsg());
        }
        //密码加密
        userInfoModel.setPassword(MSUtils.EncodeByMD5(userInfoModel.getPassword()));
        try {
            userInfoModel.setId(UUID.randomUUID().toString());
            userInfoMapper.save(this.convertUserInfoFormUserInfoModel(userInfoModel));
            userPassMapper.save(this.convertUserInfoFormUserPass(userInfoModel));
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.USER_ADD_ERROR);
        }
    }

    /**
     * 判断手机号不重复
     *
     * @param telPhone 手机号
     * @return 是否重复
     */
    @Override
    public boolean checkTelPhone(String telPhone) {
        List<UserInfo> userInfos = userInfoMapper.findUserInfoByTelPhone(telPhone);
        return null != userInfos && userInfos.size() > 0;
    }


    /**
     * 用户登录
     *
     * @param name     用户名
     * @param password 密码
     * @return 用户模型
     * @throws BusinessException 异常
     */
    @Override
    public UserInfoModel getLoginUserInfoModel(String name, String password) throws BusinessException {
        UserInfo userInfo = userInfoMapper.findUserInfoByName(name);
        if (null != userInfo) {
            UserPass userPass = userPassMapper.findUserPassByUserId(userInfo.getId());
            if (null == userPass) throw new BusinessException(EmBusinessError.USER_NAME_PASS_ERROR);
            if (!StringUtils.equals(password, userPass.getPassword())) {
                throw new BusinessException(EmBusinessError.USER_NAME_PASS_ERROR);
            }
            UserInfoModel userInfoModel = this.convertUserInfoModelFormUserInfo(userInfo);
            userInfoModel.setPassword(userPass.getPassword());
            return userInfoModel;
        } else {
            return null;
        }
    }


    /**
     * 用户信息 转 用户模型
     *
     * @param userInfo 用户信息
     * @return 用户模型
     */
    private UserInfoModel convertUserInfoModelFormUserInfo(UserInfo userInfo) {
        if (null == userInfo) {
            return null;
        }
        UserInfoModel userInfoModel = new UserInfoModel();
        BeanUtils.copyProperties(userInfo, userInfoModel);
        return userInfoModel;
    }

    /**
     * 用户模型 转 用户信息
     *
     * @param userInfoModel 用户模型
     * @return 用户信息
     */
    private UserInfo convertUserInfoFormUserInfoModel(UserInfoModel userInfoModel) {
        if (null == userInfoModel) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoModel, userInfo);
        return userInfo;
    }

    /**
     * 用户模型 转 用户密码
     *
     * @param userInfoModel 用户模型
     * @return 用户密码
     */
    private UserPass convertUserInfoFormUserPass(UserInfoModel userInfoModel) {
        if (StringUtils.isEmpty(userInfoModel.getPassword())) {
            return null;
        }
        UserPass userPass = new UserPass();
        userPass.setPassword(userInfoModel.getPassword());
        userPass.setUserId(userInfoModel.getId());
        userPass.setId(userInfoModel.getId());
        return userPass;
    }
}
