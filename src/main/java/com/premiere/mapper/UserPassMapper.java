package com.premiere.mapper;

import com.premiere.dto.UserPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserPassMapper extends JpaRepository<UserPass,String>, JpaSpecificationExecutor<UserPass> {

    UserPass findUserPassByUserId(String userId);
}
