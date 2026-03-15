//package com.exchange.backend.service;
//
//import com.exchange.backend.dto.PlaceOrderRequest;
//import com.exchange.backend.enums.OrderMode;
//import com.exchange.backend.enums.OrderType;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Random;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Simulates trading activity automatically.
// *
// * This service creates random BUY and SELL orders
// * using multiple threads to simulate real market activity.
// *
// * When backend starts, this simulator will continuously
// * generate orders and push them into the Matching Engine.
// */
//@Service
//@RequiredArgsConstructor
//public class OrderSimulatorService {
//
//    private final OrderService orderService;
//
//    private final Random random = new Random();
//
//    private final String[] symbols = {"TCS", "INFY"};
//
//    /**
//     * Automatically start simulation when backend starts
//     */
//    @PostConstruct
//    public void startSimulation() {
//        System.out.println("🚀 Mini Stock Exchange Backend Started");
//        System.out.println("📡 WebSocket enabled");
//        System.out.println("⚡ Matching Engine running");
//        System.out.println("🚀 Starting Trading Simulator...");
//
//
//        ExecutorService executor = Executors.newFixedThreadPool(3);
//
//        for (int i = 0; i < 10; i++) {
//
//            executor.submit(() -> {
//
//                while (true) {
//
//                    try {
//
//                        PlaceOrderRequest request = generateRandomOrder();
//
//                        orderService.placeOrder(request);
//
//                        Thread.sleep(800 + random.nextInt(800));
////                    } catch (Exception e) {
//                        // ignore validation failures
//                    }
//
//                }
//
//            });
//
//        }
//
//    }
//
//    /**
//     * Generates random BUY / SELL orders
//     */
//    private PlaceOrderRequest generateRandomOrder() {
//
//        OrderType type =
//                random.nextBoolean() ? OrderType.BUY : OrderType.SELL;
//
//        String symbol =
//                symbols[random.nextInt(symbols.length)];
//
//        int quantity =
//                random.nextInt(10) + 1;
//
//        double price =
//                3400 + random.nextInt(200);
//
//        Long userId =
//                random.nextBoolean() ? 1L : 2L;
//
//        PlaceOrderRequest request = new PlaceOrderRequest();
//
//        request.setUserId(userId);
//        request.setStockSymbol(symbol);
//        request.setType(type);
//        request.setOrderMode(OrderMode.LIMIT);
//        request.setQuantity(quantity);
//        request.setPrice(price);
//
//        return request;
//    }
//
//}