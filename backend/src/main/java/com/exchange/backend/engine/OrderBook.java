package com.exchange.backend.engine;

import com.exchange.backend.model.Order;
import com.exchange.backend.enums.OrderType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class OrderBook {

    // BUY order books per stock
    private final Map<String, PriorityBlockingQueue<Order>> buyOrders = new ConcurrentHashMap<>();

    // SELL order books per stock
    private final Map<String, PriorityBlockingQueue<Order>> sellOrders = new ConcurrentHashMap<>();


    private PriorityBlockingQueue<Order> createBuyQueue() {

        return new PriorityBlockingQueue<>(
                100,
                Comparator
                        .comparing(Order::getPrice).reversed()
                        .thenComparing(Order::getCreatedAt)
        );
    }


    private PriorityBlockingQueue<Order> createSellQueue() {

        return new PriorityBlockingQueue<>(
                100,
                Comparator
                        .comparing(Order::getPrice)
                        .thenComparing(Order::getCreatedAt)
        );
    }


    public void addOrder(Order order) {

        String symbol = order.getStockSymbol();

        if (order.getType() == OrderType.BUY) {

            buyOrders
                    .computeIfAbsent(symbol, k -> createBuyQueue())
                    .add(order);

        } else {

            sellOrders
                    .computeIfAbsent(symbol, k -> createSellQueue())
                    .add(order);
        }
    }


    // KEEP SAME METHOD NAME
    public PriorityBlockingQueue<Order> getBuyOrders(String symbol) {

        return buyOrders.computeIfAbsent(symbol, k -> createBuyQueue());
    }


    // KEEP SAME METHOD NAME
    public PriorityBlockingQueue<Order> getSellOrders(String symbol) {

        return sellOrders.computeIfAbsent(symbol, k -> createSellQueue());
    }


    public void printOrderBook(String symbol) {

        System.out.println("========== ORDER BOOK (" + symbol + ") ==========");

        PriorityBlockingQueue<Order> buyQueue = getBuyOrders(symbol);
        PriorityBlockingQueue<Order> sellQueue = getSellOrders(symbol);

        System.out.println("BUY ORDERS:");

        if (buyQueue.isEmpty()) {
            System.out.println("None");
        } else {

            buyQueue.forEach(order ->
                    System.out.println(
                            order.getStockSymbol() +
                                    " Qty:" + order.getQuantity() +
                                    " Price:" + order.getPrice()
                    )
            );
        }

        System.out.println("-------------------------------");

        System.out.println("SELL ORDERS:");

        if (sellQueue.isEmpty()) {
            System.out.println("None");
        } else {

            sellQueue.forEach(order ->
                    System.out.println(
                            order.getStockSymbol() +
                                    " Qty:" + order.getQuantity() +
                                    " Price:" + order.getPrice()
                    )
            );
        }

        System.out.println("================================");
    }


    public void removeOrder(Order order) {

        String symbol = order.getStockSymbol();

        if (order.getType() == OrderType.BUY) {

            PriorityBlockingQueue<Order> queue = buyOrders.get(symbol);

            if (queue != null) {
                queue.remove(order);
            }

        } else {

            PriorityBlockingQueue<Order> queue = sellOrders.get(symbol);

            if (queue != null) {
                queue.remove(order);
            }
        }
    }

}