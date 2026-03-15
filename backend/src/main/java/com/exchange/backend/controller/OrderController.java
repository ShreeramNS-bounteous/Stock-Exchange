package com.exchange.backend.controller;

import com.exchange.backend.dto.OrderBookResponse;
import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.dto.PortfolioResponse;
import com.exchange.backend.dto.TradeResponse;
import com.exchange.backend.model.Order;
import com.exchange.backend.model.User;
import com.exchange.backend.repository.UserRepository;
import com.exchange.backend.service.OrderBookService;
import com.exchange.backend.service.OrderService;
import com.exchange.backend.service.PortfolioService;
import com.exchange.backend.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderBookService orderBookService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    @PostMapping("/orders")
    public ResponseEntity<String> placeOrder(
            @RequestBody PlaceOrderRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        orderService.placeOrder(user.getId(), request);

        return ResponseEntity.ok("Order received");
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.ok("Order cancelled");
    }

    @GetMapping("/orderbook")
    public ResponseEntity<OrderBookResponse> getOrderBook(@RequestParam String symbol) {

        return ResponseEntity.ok(orderBookService.getOrderBook(symbol));
    }

    @GetMapping("/trades")
    public ResponseEntity<List<TradeResponse>> getTrades() {

        return ResponseEntity.ok(tradeService.getAllTrades());
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioResponse>> getMyPortfolio(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                portfolioService.getUserPortfolio(user.getId())
        );
    }

    @GetMapping("/trades/{symbol}")
    public ResponseEntity<List<TradeResponse>> getTradesBySymbol(
            @PathVariable String symbol) {

        return ResponseEntity.ok(
                tradeService.getTradesBySymbol(symbol)
        );
    }

    @GetMapping("/orders/my")
    public ResponseEntity<List<Order>> getMyOpenOrders(
            Authentication authentication){

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                orderService.getOpenOrders(user.getId())
        );
    }


    @GetMapping("/orders/history")
    public ResponseEntity<List<Order>> getOrderHistory(
            Authentication authentication){

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                orderService.getOrderHistory(user.getId())
        );
    }

    @GetMapping("/trades/my")
    public ResponseEntity<List<TradeResponse>> getMyTrades(
            Authentication authentication){

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                tradeService.getUserTrades(user.getId())
        );
    }

}