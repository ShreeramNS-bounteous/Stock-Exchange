package com.exchange.backend.engine;

import com.exchange.backend.model.Order;
import com.exchange.backend.enums.OrderType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.PriorityQueue;

@Component
public class OrderBook {

    // Highest BUY price first
    private final PriorityQueue<Order> buyOrders =
            new PriorityQueue<>(Comparator.comparing(Order::getPrice).reversed());

    // Lowest SELL price first
    private final PriorityQueue<Order> sellOrders =
            new PriorityQueue<>(Comparator.comparing(Order::getPrice));

    public void addOrder(Order order) {

        if (order.getType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public PriorityQueue<Order> getBuyOrders() {
        return buyOrders;
    }

    public PriorityQueue<Order> getSellOrders() {
        return sellOrders;
    }
}