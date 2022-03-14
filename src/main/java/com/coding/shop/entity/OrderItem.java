package com.coding.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 하나의 상품은 여러 주문 상품으로 들어갈 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // 주문 아이템 : 실제 아이템  = 다 : 1
    @JoinColumn(name="item_id")
    private Item Item;


    // 한번의 주문에 여러개의 상품을 주문 할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // 주문 아이템 : 주문내역  = 다 : 1
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;

    // 같은 상품 여러개 주문시 그 개수
    private int count;

    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
