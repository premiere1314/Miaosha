package com.premiere.mapper;

import com.premiere.dto.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PromoMapper extends JpaRepository<Promo,String>, JpaSpecificationExecutor<Promo> {

    List<Promo> findPromoByItemId(String itemId);
}
