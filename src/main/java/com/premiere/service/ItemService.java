package com.premiere.service;

import com.premiere.service.model.ItemModel;
import com.premiere.utils.error.BusinessException;

import java.util.List;

public interface ItemService {
    //增加商品
    ItemModel insertItemModel(ItemModel itemModel) throws BusinessException;
    //查询商品
    List<ItemModel> qusertItemModelsList();
    //商品性详情
    ItemModel qusertItemModelById(String id);
    //修改商品
    ItemModel updateItemModelById(ItemModel itemModel) throws BusinessException;
    //删除商品
    boolean deleteItemModelById(String id) throws BusinessException;

    void lockItem(String itemId, Integer amount);

    boolean decreaseItemStock(String itemId, Integer amount);

    boolean addSalesConunt(String itemId, Integer amount);
}
