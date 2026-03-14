package com.exchange.backend.service;

import com.exchange.backend.dto.OrderBookResponse;
import com.exchange.backend.dto.OrderLevel;
import com.exchange.backend.engine.OrderBook;
import com.exchange.backend.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class OrderBookService {

    private final OrderBook orderBook;

    public OrderBookResponse getOrderBook(String symbol) {

        Map<Double, Integer> buyMap = new TreeMap<>(Comparator.reverseOrder());
        Map<Double, Integer> sellMap = new TreeMap<>();

        for (Order order : orderBook.getBuyOrders(symbol)) {

            buyMap.put(
                    order.getPrice(),
                    buyMap.getOrDefault(order.getPrice(),0) + order.getQuantity()
            );
        }

        for (Order order : orderBook.getSellOrders(symbol)) {

            sellMap.put(
                    order.getPrice(),
                    sellMap.getOrDefault(order.getPrice(),0) + order.getQuantity()
            );
        }

        List<OrderLevel> buyLevels = buyMap.entrySet()
                .stream()
                .map(e -> new OrderLevel(e.getKey(), e.getValue()))
                .toList();

        List<OrderLevel> sellLevels = sellMap.entrySet()
                .stream()
                .map(e -> new OrderLevel(e.getKey(), e.getValue()))
                .toList();

        return new OrderBookResponse(buyLevels, sellLevels);
    }
}