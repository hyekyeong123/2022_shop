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
    private Item item;

    // 같은 상품 여러개 주문시 그 개수
    private int count;

    // 장바구니에 담을 상품 엔티티를 생성
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 장바구니에 기존에 있던 상품을 추가로 담을 경우
    public void addCount(int count){
        this.count += count;
    }

    // 상품 수량 변경ㅅ;
    public void updateCount(int count){
        this.count = count;
    }
}
