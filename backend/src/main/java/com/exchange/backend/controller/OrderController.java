package com.exchange.backend.controller;

import com.exchange.backend.dto.OrderBookResponse;
import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.model.Portfolio;
import com.exchange.backend.model.Trade;
import com.exchange.backend.repository.PortfolioRepository;
import com.exchange.backend.repository.TradeRepository;
import com.exchange.backend.service.OrderBookService;
import com.exchange.backend.service.OrderService;
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
    private final TradeRepository tradeRepository;
    private final PortfolioRepository portfolioRepository;

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
    public ResponseEntity<List<Trade>> getTrades() {

        return ResponseEntity.ok(tradeRepository.findAll());
    }

    @GetMapping("/portfolio/{userId}")
    public ResponseEntity<List<Portfolio>> getPortfolio(@PathVariable Long userId) {

        return ResponseEntity.ok(
                portfolioRepository.findAll()
                        .stream()
                        .filter(p -> p.getUser().getId().equals(userId))
                        .toList()
        );
    }

}