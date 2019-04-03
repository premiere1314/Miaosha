package com.premiere.service.impl;

import com.premiere.dto.OrderInfo;
import com.premiere.mapper.OrderInfoMapper;
import com.premiere.service.ItemService;
import com.premiere.service.OrderInfoService;
import com.premiere.service.PromoService;
import com.premiere.service.UserInfoService;
import com.premiere.service.model.ItemModel;
import com.premiere.service.model.OrderModel;
import com.premiere.service.model.PromoModel;
import com.premiere.service.model.UserInfoModel;
import com.premiere.utils.MSUtils;
import com.premiere.utils.error.BusinessException;
import com.premiere.utils.error.EmBusinessError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService {

    private final OrderInfoMapper orderInfoMapper;
    private final UserInfoService userInfoService;
    private final ItemService itemService;
    private final PromoService promoService;

    @Autowired
    public OrderInfoServiceImpl(OrderInfoMapper orderInfoMapper, ItemService itemService, UserInfoService userInfoService, PromoService promoService) {
        this.orderInfoMapper = orderInfoMapper;
        this.itemService = itemService;
        this.userInfoService = userInfoService;
        this.promoService = promoService;
    }

    /**
     * 下单
     * @param promoId 订单ID
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param amount 商品购买数量
     * @return 订单模型
     * @throws BusinessException 异常
     */
    @Override
    public OrderModel insertOrderModel(String promoId, String userId, String itemId, Integer amount) throws BusinessException {
        //判断用火是否存或登录
        UserInfoModel userInfo = userInfoService.getUserInfo(userId);
        if (null == userInfo) {
            throw new BusinessException(EmBusinessError.USER_IS_NULL);
        }
        //判断商品时候否存在
        ItemModel itemModel = itemService.qusertItemModelById(itemId);
        if (null == itemModel) {
            throw new BusinessException(EmBusinessError.ITEM_IS_NULL);
        }
        //判断库存是否充足
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.ITEM_NUM_ERROR);
        }
        if (!itemService.decreaseItemStock(itemId, amount)) {
            throw new BusinessException(EmBusinessError.ITEM_NUM_ERROR);
        }

        //销量增加
        if (!itemService.addSalesConunt(itemId, amount)) {
            throw new BusinessException(EmBusinessError.ITEM_NUM_ERROR);
        }
        OrderModel orderModel = new OrderModel();
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setUserId(userId);

        PromoModel promoModel = promoService.qusertPromoByItemId(promoId);
        if (null != promoModel && promoModel.getStatus() == 2) {//秒杀下单的逻辑
            //查询秒杀活动的价格
            orderModel.setItemPrice(promoModel.getPromoItemPrice());
            orderModel.setOrderPrice(promoModel.getPromoItemPrice().multiply(new BigDecimal(amount)));
        } else {//正常下单的逻辑
            orderModel.setItemPrice(itemModel.getPrice());
            orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
        }
        //上传订单的流水号
        orderModel.setId(MSUtils.generaterOrderNo());
        //把数据放进 OrderVo
        OrderInfo orderInfo = this.convertOrderInfoFromOrderModel(orderModel);

        orderInfoMapper.save(orderInfo);
        return orderModel;
    }


    /**
     * 订单模型 转 订单信息
     * @param orderModel 订单模型
     * @return 订单信息
     */
    private OrderInfo convertOrderInfoFromOrderModel(OrderModel orderModel) {
        if (null == orderModel) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderModel, orderInfo);
        orderInfo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderInfo.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(orderModel.getAmount())));
        return orderInfo;
    }

}
