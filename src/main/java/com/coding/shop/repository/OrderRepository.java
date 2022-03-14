package com.coding.shop.repository;

import com.coding.shop.entity.Cart;
import com.coding.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
