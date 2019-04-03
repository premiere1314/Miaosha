package com.premiere.service.impl;

import com.premiere.dto.Item;
import com.premiere.dto.ItemStock;
import com.premiere.mapper.ItemMapper;
import com.premiere.mapper.ItemStockMapper;
import com.premiere.service.ItemService;
import com.premiere.service.model.ItemModel;
import com.premiere.utils.error.BusinessException;
import com.premiere.utils.error.EmBusinessError;
import com.premiere.utils.validator.ValidatorImpl;
import com.premiere.utils.validator.ValidatorResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemStockMapper itemStockMapper;
    private final ValidatorImpl validator;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, ItemStockMapper itemStockMapper, ValidatorImpl validator) {
        this.itemMapper = itemMapper;
        this.itemStockMapper = itemStockMapper;
        this.validator = validator;
    }

    /**
     * 添加商品
     *
     * @param itemModel 商品模型
     * @return 商品模型
     * @throws BusinessException 异常
     */
    @Override
    public ItemModel insertItemModel(ItemModel itemModel) throws BusinessException {
        itemModel.setId(UUID.randomUUID().toString());
        ValidatorResult result = this.validator.validatorResult(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.UN_ERROR, result.getErrorMsg());
        }
        try {
            Item item = this.convertItemFromItemModel(itemModel);
            itemMapper.save(item);
            ItemStock itemStock = this.convertItemStockFromItemModel(itemModel);
            itemStock.setId(UUID.randomUUID().toString());
            itemStockMapper.save(itemStock);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.UN_ERROR, "商品添加失败");
        }
        return this.qusertItemModelById(itemModel.getId());
    }

    /**
     * 查询商品列表
     *
     * @return 商品列表
     */
    @Override
    public List<ItemModel> qusertItemModelsList() {
        List<Item> items = itemMapper.findAll();
        List<ItemModel> itemModels = new ArrayList<>();
        if (items != null && items.size() > 0) {
            for (Item item : items) {
                ItemStock itemStock = itemStockMapper.findItemStoreByItemId(item.getId());
                ItemModel itemModel = this.convertItemModelFromItemAndItemStock(item, itemStock);
                if (null != itemModel) {
                    itemModels.add(itemModel);
                }
            }
        }
        return itemModels;
    }

    /**
     * 根据商品ID查询商品和库存
     *
     * @param id 商品ID
     * @return 商品模型
     */
    @Override
    public ItemModel qusertItemModelById(String id) {
        Item item = itemMapper.findItemById(id);
        ItemStock itemStock = itemStockMapper.findItemStoreByItemId(id);
        return this.convertItemModelFromItemAndItemStock(item, itemStock);
    }

    /**
     * 修改商品
     *
     * @param itemModel 商品模型
     * @return 修改后的商品模型
     * @throws BusinessException 异常
     */
    @Override
    public ItemModel updateItemModelById(ItemModel itemModel) throws BusinessException {
        ValidatorResult result = this.validator.validatorResult(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.UN_ERROR, result.getErrorMsg());
        }
        try {
            Item item = this.convertItemFromItemModel(itemModel);
            itemMapper.save(item);
            ItemStock itemStock = itemStockMapper.findItemStoreByItemId(item.getId());
            itemStock.setStock(itemModel.getStock());
            itemStockMapper.save(itemStock);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.UN_ERROR, "商品修改失败");
        }
        return null;
    }


    /**
     * 删除商品
     *
     * @param id 商品ID
     * @return 成功或失败
     * @throws BusinessException 异常
     */
    @Override
    public boolean deleteItemModelById(String id) throws BusinessException {
        try {
            itemMapper.deleteById(id);
            itemStockMapper.deleteItemStockByItemId(id);
            return true;
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.UN_ERROR);
        }
    }

    /**
     * 行级锁
     *
     * @param itemId 商品ID
     * @param amount 商品数量
     */
    @Override
    public void lockItem(String itemId, Integer amount) {
        //对表进行锁定
        //itemStockMapper.lockItem("order_info");

    }

    /**
     * 下单时判断库存是否充足
     *
     * @param itemId 商品ID
     * @param amount 商品购买数量
     * @return 是否成功
     */
    @Override
    public boolean decreaseItemStock(String itemId, Integer amount) {
        //如果库存不足
        ItemStock itemStock = itemStockMapper.findItemStoreByItemId(itemId);
        if (amount <= itemStock.getStock()) {
            //可以优化的地方
            itemStock.setStock(itemStock.getStock() - amount);
            return null != itemStockMapper.save(itemStock);
        }
        return false;
    }

    /**
     * 下单成功后增加销量
     *
     * @param itemId 商品ID
     * @param amount 商品数量
     * @return 是否成功
     */
    @Override
    public boolean addSalesConunt(String itemId, Integer amount) {
        ItemModel itemModel = this.qusertItemModelById(itemId);
        if (null != itemModel) {
            itemModel.setSales(itemModel.getSales() + amount);
            return null != itemMapper.save(this.convertItemFromItemModel(itemModel));
        }
        return false;
    }

    /**
     * 商品 和  商品库存 转 数据模型
     *
     * @param item      商品
     * @param itemStock 商品库存
     * @return 数据模型
     */
    private ItemModel convertItemModelFromItemAndItemStock(Item item, ItemStock itemStock) {
        if (null == item || null == itemStock) {
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item, itemModel);
        itemModel.setPrice(new BigDecimal(item.getPrice()));
        itemModel.setStock(itemStock.getStock());
        return itemModel;
    }

    /**
     * 数据模型 转 商品
     *
     * @param itemModel 数据模型
     * @return 商品
     */
    private Item convertItemFromItemModel(ItemModel itemModel) {
        if (null == itemModel) {
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel, item);
        item.setPrice(itemModel.getPrice().doubleValue());
        return item;
    }

    /**
     * 数据模型 转 商品库存
     *
     * @param itemModel 数据模型
     * @return 商品库存
     */
    private ItemStock convertItemStockFromItemModel(ItemModel itemModel) {
        if (null == itemModel) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        BeanUtils.copyProperties(itemModel, itemStock);
        itemStock.setItemId(itemModel.getId());
        return itemStock;
    }
}
