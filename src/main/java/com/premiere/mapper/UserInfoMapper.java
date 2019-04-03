package com.premiere.mapper;

import com.premiere.dto.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserInfoMapper extends JpaRepository<UserInfo,String>, JpaSpecificationExecutor<UserInfo> {

    UserInfo findUserInfoById(String id);

    UserInfo findUserInfoByName(String name);

    List<UserInfo> findUserInfoByTelPhone(String telPhone);
}
