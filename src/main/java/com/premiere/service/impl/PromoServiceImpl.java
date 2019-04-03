package com.premiere.service.impl;

import com.premiere.dto.Promo;
import com.premiere.mapper.PromoMapper;
import com.premiere.service.PromoService;
import com.premiere.service.model.PromoModel;
import com.premiere.utils.error.BusinessException;
import com.premiere.utils.error.EmBusinessError;
import com.premiere.utils.validator.ValidatorImpl;
import com.premiere.utils.validator.ValidatorResult;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromoServiceImpl implements PromoService {

    private final PromoMapper promoMapper;
    private final ValidatorImpl validator;

    @Autowired
    public PromoServiceImpl(PromoMapper promoMapper, ValidatorImpl validator) {
        this.promoMapper = promoMapper;
        this.validator = validator;
    }

    /**
     * 根据商品ID查询活动
     *
     * @param itemId 商品ID
     * @return 活动详细信息
     */
    @Override
    public PromoModel qusertPromoByItemId(String itemId) {
        List<Promo> promos = promoMapper.findPromoByItemId(itemId);
        if (promos != null && promos.size() > 0) {
            Promo promo = promos.get(0);
            PromoModel promoModel = this.convertPromoModelFromPromo(promo);
            if (null == promoModel) {
                return null;
            }
            //判断秒杀活动的时间 和状态
            if (promoModel.getStartDate().isAfterNow()) {
                promoModel.setStatus(1);
            } else if (promoModel.getEndDate().isBeforeNow()) {
                promoModel.setStatus(3);
            } else {
                promoModel.setStatus(2);
            }
            return promoModel;
        }
        return null;
    }

    /**
     * 添加活动
     *
     * @param promoModel 活动模型
     * @return 活动模型
     * @throws BusinessException ..
     */
    @Override
    public PromoModel insertPromoModel(PromoModel promoModel) throws BusinessException {
        ValidatorResult result = validator.validatorResult(promoModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.UN_ERROR, result.getErrorMsg());
        }

        Promo promo = this.convertPromoFromPromoModel(promoModel);
        if (null!=promoMapper.save(promo)) {
            return promoModel;
        }
        return null;
    }


    /**
     * 活动模型 转 活动类
     *
     * @param promoModel 活动模型
     * @return Promo 活动类
     */
    private Promo convertPromoFromPromoModel(PromoModel promoModel) {
        if (null == promoModel) {
            return null;
        }
        Promo promo = new Promo();
        BeanUtils.copyProperties(promoModel, promo);
        //价格转换
        promo.setPromoItemPrice(promoModel.getPromoItemPrice().doubleValue());
        //时间转换
        promo.setStartTime(promoModel.getStartDate().toDate());
        promo.setEndDate(promoModel.getEndDate().toDate());
        return promo;
    }

    /**
     * 活动类 转 活动模型
     *
     * @param promo 活动类
     * @return 活动模型
     */
    private PromoModel convertPromoModelFromPromo(Promo promo) {
        if (promo == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promo, promoModel);
        //价格转换
        promoModel.setPromoItemPrice(new BigDecimal(promo.getPromoItemPrice()));
        //时间转换
        promoModel.setStartDate(new DateTime(promo.getStartTime()));
        promoModel.setEndDate(new DateTime(promo.getEndDate()));
        return promoModel;
    }
}
