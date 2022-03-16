package com.coding.shop.dto;

import com.coding.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; // 상품명
    private int count; // 주문 수량
    private int orderPrice; // 주문 금액
    private String imgUrl; // 상품 이미지 경로
}
