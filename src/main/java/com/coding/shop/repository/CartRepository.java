package com.coding.shop.repository;

import com.coding.shop.entity.Cart;
import com.coding.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CartRepository extends JpaRepository<Cart, Long>{

    // 로그인한 회원의 Cart Entity 찾기기
   Cart findByMemberId(Long memberId);
}
