package com.coding.shop.repository;

import com.coding.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    // 상품의 대표이미지 조회
    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);
}
