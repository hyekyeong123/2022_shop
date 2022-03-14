package com.coding.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

// 하나의 카트에 여러개의 아이템이 들어갈 수 있다.
// 같은 상품을 여러개 주문할 수 있다.

@Entity
@Table(name="cart_item")
@Getter @Setter
public class CartItem extends BaseEntity {

    @Id
    @Column(name="cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY) // 카트 아이템 : 카트  = 다 : 1
    @JoinColumn(name="cart_id")
    private Cart cart;

    // 장바구니에 담을 상품의 정보
    @OneToOne(fetch = FetchType.LAZY) // 카트 아이템 : 실제 아이템  = 1 : 1
    @JoinColumn(name="item_id")
    private Item Item;

    // 같은 상품 여러개 주문시 그 개수
    private int count;
}
