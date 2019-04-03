package com.premiere.controller;

import com.premiere.service.PromoService;
import com.premiere.service.model.PromoModel;
import com.premiere.utils.CommonReturnType;
import com.premiere.utils.error.BusinessException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/promo")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class PromoController extends BaseController {

    private final PromoService promoService;

    @Autowired
    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }


    /**
     * 添加商品活动
     * @param promoName 活动名称
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param itemId 商品ID
     * @param promoItemPrice 商品秒杀价格
     * @return 页面数据
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "/insertPromoModel", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType insertPromoModel(@RequestParam String promoName,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               @RequestParam String itemId,
                               @RequestParam Double promoItemPrice) throws BusinessException {
        PromoModel promoModel = new PromoModel();
        promoModel.setId(UUID.randomUUID().toString());
        promoModel.setPromoName(promoName);
        promoModel.setPromoItemPrice(new BigDecimal(promoItemPrice));
        promoModel.setStartDate(DateTime.parse(startDate,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        promoModel.setEndDate(DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        promoModel.setItemId(itemId);
        return CommonReturnType.create(promoService.insertPromoModel(promoModel));
    }
}
