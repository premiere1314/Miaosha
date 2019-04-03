package com.premiere.mapper;

import com.premiere.dto.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemStockMapper extends JpaRepository<ItemStock,String>, JpaSpecificationExecutor<ItemStock> {

    ItemStock findItemStoreByItemId(String itemId);

    void deleteItemStockByItemId(String itemId);
}
