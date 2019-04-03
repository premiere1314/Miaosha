package com.premiere.service;

import com.premiere.dto.Promo;
import com.premiere.service.model.PromoModel;
import com.premiere.utils.error.BusinessException;

public interface PromoService {

    PromoModel qusertPromoByItemId(String itemId);

    PromoModel insertPromoModel(PromoModel promoModel) throws BusinessException;
}
