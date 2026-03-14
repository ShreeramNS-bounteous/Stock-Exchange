package com.exchange.backend.engine;

import com.exchange.backend.model.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class SymbolOrderRouter {

    private final Map<String, BlockingQueue<Order>> symbolQueues = new ConcurrentHashMap<>();

    public void routeOrder(Order order) {

        String symbol = order.getStockSymbol();

        BlockingQueue<Order> queue =
                symbolQueues.computeIfAbsent(
                        symbol,
                        s -> new LinkedBlockingQueue<>()
                );

        queue.offer(order);
    }

    public BlockingQueue<Order> getQueue(String symbol) {

        return symbolQueues.computeIfAbsent(
                symbol,
                s -> new LinkedBlockingQueue<>()
        );
    }

    public Map<String, BlockingQueue<Order>> getAllQueues() {
        return symbolQueues;
    }
}