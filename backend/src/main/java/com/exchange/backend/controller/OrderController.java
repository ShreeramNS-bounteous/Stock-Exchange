package com.exchange.backend.controller;

import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody PlaceOrderRequest request) {

        orderService.placeOrder(request);

        return ResponseEntity.ok("Order received");
    }
}