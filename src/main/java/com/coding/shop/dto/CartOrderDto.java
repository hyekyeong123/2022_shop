package com.coding.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    // 여러 개의 상품을 주문하므로 자기 자신을 리스트로 저장
    private List<CartOrderDto> cartOrderDtoList;

}