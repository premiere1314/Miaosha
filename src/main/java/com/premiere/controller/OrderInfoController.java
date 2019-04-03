package com.premiere.controller;

import com.premiere.service.OrderInfoService;
import com.premiere.service.PromoService;
import com.premiere.service.model.PromoModel;
import com.premiere.service.model.UserInfoModel;
import com.premiere.utils.CommonReturnType;
import com.premiere.utils.error.BusinessException;
import com.premiere.utils.error.EmBusinessError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderInfoController extends BaseController{

    private final OrderInfoService service;
    private final HttpServletRequest request;
    private final PromoService promoService;

    @Autowired
    public OrderInfoController(OrderInfoService service, HttpServletRequest request, PromoService promoService) {
        this.service = service;
        this.request = request;
        this.promoService = promoService;
    }

    /**
     *
     * @param itemId 商品ID
     * @param amount 商品数量
     * @return 页面返回数据
     * @throws BusinessException 抛出异常
     */
    @RequestMapping(value = "/insertOrderModel", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType insertOrderModel(@RequestParam String itemId,
                                             @RequestParam Integer amount) throws BusinessException {
        boolean flag =(boolean)request.getSession().getAttribute("IS_LOGIN");
        if (!flag){
            throw new BusinessException(EmBusinessError.USER_IS_NULL);
        }
        UserInfoModel userInfoModel= (UserInfoModel)request.getSession().getAttribute("USER_INFO");
        if (null==userInfoModel){
            throw new BusinessException(EmBusinessError.USER_IS_NULL);
        }
        PromoModel promoModel = promoService.qusertPromoByItemId(itemId);
        if (null!=promoModel){
            service.insertOrderModel(promoModel.getId(),userInfoModel.getId(),itemId,amount);
        }else {
            service.insertOrderModel(null,userInfoModel.getId(),itemId,amount);
        }
        return CommonReturnType.create("sucess");
}


}
