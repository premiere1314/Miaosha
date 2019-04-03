package com.premiere.service;

import com.premiere.service.model.OrderModel;
import com.premiere.utils.error.BusinessException;

public interface OrderInfoService {

    OrderModel insertOrderModel(String promoId, String userId, String itemId, Integer amount) throws BusinessException;


}
