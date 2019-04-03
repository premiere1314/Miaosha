package com.premiere.mapper;

import com.premiere.dto.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderInfoMapper extends JpaRepository<OrderInfo,String>, JpaSpecificationExecutor<OrderInfo> {
}
