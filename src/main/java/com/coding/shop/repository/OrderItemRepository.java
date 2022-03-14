package com.coding.shop.repository;

import com.coding.shop.entity.Order;
import com.coding.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
