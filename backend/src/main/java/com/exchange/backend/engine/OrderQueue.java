package com.exchange.backend.engine;

import com.exchange.backend.model.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class OrderQueue {

    private final ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<>();

    public void addOrder(Order order) {
        queue.add(order);
    }

    public Order pollOrder() {
        return queue.poll();
    }
}