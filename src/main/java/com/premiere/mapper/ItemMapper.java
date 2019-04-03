package com.premiere.mapper;

import com.premiere.dto.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemMapper extends JpaRepository<Item,String>, JpaSpecificationExecutor<Item> {

    Item findItemById(String id);
}
