package com.exchange.backend.engine;

import com.exchange.backend.model.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class OrderQueue {

    private final BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    public void addOrder(Order order) {
        queue.offer(order);
    }

    public Order pollOrder() {

        try {
            return queue.take(); // blocks until order arrives
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}