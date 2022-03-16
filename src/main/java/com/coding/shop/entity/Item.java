package com.coding.shop.entity;

import com.coding.shop.constant.ItemSellStatus;
import com.coding.shop.dto.ItemFormDto;
import com.coding.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter @Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 상품을 주문할 경우 재고를 감소
    public void removeStock(int orderNumber){
        int restStock = this.stockNumber - orderNumber;

        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 "+ this.stockNumber+")");
        }

        this.stockNumber = restStock;
    }

    // 주문을 취소할 경우 재고를 증가
    public void addStock(int orderedNumber){
        this.stockNumber += orderedNumber;
    }
}
