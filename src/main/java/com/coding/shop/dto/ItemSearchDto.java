package com.coding.shop.dto;

import com.coding.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    // 현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회
    private String searchDateType;
    // all - 상품 등록일 전체
    // 1d - 최근 하루 동안 등록된 상품
    // 1w - 최근 일주일 동안 등록된 상품
    // 1m - 최근 한달 동안 등록된 상품
    // 6m - 최근 6개월 동안 등록된 상품

    // 상품의 판매 상태
    private ItemSellStatus searchSellStatus;

    // 상품을 조회할 때 어떤 유형으로 조회할지
    private String searchBy;
    // itemNm - 상품명
    // createdBy - 상품 등록자 아이디

    // 조회할 검색어 저장
    private String searchQuery="";
}
