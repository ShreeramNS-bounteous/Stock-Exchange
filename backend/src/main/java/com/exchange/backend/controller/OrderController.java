package com.exchange.backend.controller;

import com.exchange.backend.dto.OrderBookResponse;
import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.dto.PortfolioResponse;
import com.exchange.backend.dto.TradeResponse;
import com.exchange.backend.service.OrderBookService;
import com.exchange.backend.service.OrderService;
import com.exchange.backend.service.PortfolioService;
import com.exchange.backend.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderBookService orderBookService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody PlaceOrderRequest request) {

        orderService.placeOrder(request);

        return ResponseEntity.ok("Order received");
    }

    @DeleteMapping("/{orderId}")
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

    @GetMapping("/portfolio/{userId}")
    public ResponseEntity<List<PortfolioResponse>> getPortfolio(@PathVariable Long userId) {

        return ResponseEntity.ok(
                portfolioService.getUserPortfolio(userId)
        );
    }

}