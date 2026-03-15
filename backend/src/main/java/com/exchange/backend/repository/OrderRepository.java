package com.exchange.backend.repository;

import com.exchange.backend.enums.OrderStatus;
import com.exchange.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    List<Order> findByUserId(Long userId);
}
