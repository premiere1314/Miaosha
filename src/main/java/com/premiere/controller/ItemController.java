package com.premiere.controller;

import com.premiere.controller.vo.ItemVo;
import com.premiere.service.ItemService;
import com.premiere.service.PromoService;
import com.premiere.service.model.ItemModel;
import com.premiere.service.model.PromoModel;
import com.premiere.utils.CommonReturnType;
import com.premiere.utils.MSUtils;
import com.premiere.utils.error.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {

    private final ItemService service;
    private final PromoService promoService;

    @Autowired
    public ItemController(ItemService service, PromoService promoService) {
        this.service = service;
        this.promoService = promoService;
    }

    @RequestMapping(value = "/insertItemModel", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType insertItemModel(String id,
                                            @RequestParam String title,
                                            @RequestParam String price,
                                            @RequestParam Integer stock,
                                            @RequestParam String description,
                                            @RequestParam String imgUrl) throws BusinessException {
        //封装service 用来请求商品

        ItemModel itemModel = new ItemModel();
        itemModel.setId(id);
        itemModel.setPrice(new BigDecimal(price));
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        itemModel.setTitle(title);
        itemModel.setSales(0);

        ItemModel item = service.insertItemModel(itemModel);
        return CommonReturnType.create(this.convertItemVoFromItemModel(item));
    }

    @RequestMapping(value = "/qusertItemModelsList", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType qusertItemModelsList() {
        List<ItemModel> itemModels = service.qusertItemModelsList();
        List<ItemVo> list = new ArrayList<>();
        for (ItemModel itemModel : itemModels) {
            list.add(this.convertItemVoFromItemModel(itemModel));
        }
        return CommonReturnType.create(list);
    }


    @RequestMapping(value = "/qusertItemModelById", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType qusertItemModelById(String id) {
        if (StringUtils.isEmpty(id)) {
            return CommonReturnType.create(null);
        }
        ItemModel itemModel = service.qusertItemModelById(id);
        return CommonReturnType.create(this.convertItemVoFromItemModel(itemModel));
    }

    /**
     * 业务模型 转 传输模型
     *
     * @param itemModel 业务模型
     * @return 传输模型
     */
    private ItemVo convertItemVoFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel, itemVo);
        PromoModel promoModel = promoService.qusertPromoByItemId(itemModel.getId());
        if (null != promoModel) {
            itemVo.setPromoItemPrice(promoModel.getPromoItemPrice());
            itemVo.setStatus(promoModel.getStatus());
            itemVo.setStartDate(MSUtils.dateToStr(promoModel.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
            itemVo.setEndDate(MSUtils.dateToStr(promoModel.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
            itemVo.setPromoId(promoModel.getId());
            itemVo.setPromoName(promoModel.getPromoName());
        }
        return itemVo;
    }
}
