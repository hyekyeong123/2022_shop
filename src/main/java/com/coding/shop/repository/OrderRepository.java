package com.coding.shop.repository;

import com.coding.shop.entity.Cart;
import com.coding.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{

    // 현재 로그인한 사용자의 주문 데이터 가져오기
    @Query("select o from Order AS o " +
            "where o.member.email = :email "+
            "order by o.orderDate desc"
    )
    List<Order> findOrdersByMember (@Param("email") String email, Pageable pageable);

    // 현재 로그인한 사용자의 주문 개수 가져오기
    @Query("select count(o) from Order AS o " +
            "where o.member.email = :email"
    )
    Long countOrdersByMember(@Param("email") String email);
}
