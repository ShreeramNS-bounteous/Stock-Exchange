package com.exchange.backend.service;

import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.engine.OrderQueue;
import com.exchange.backend.enums.OrderStatus;
import com.exchange.backend.enums.OrderType;
import com.exchange.backend.model.Order;
import com.exchange.backend.model.Portfolio;
import com.exchange.backend.model.User;
import com.exchange.backend.repository.OrderRepository;
import com.exchange.backend.repository.PortfolioRepository;
import com.exchange.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderQueue orderQueue;
    private final MarketService marketService;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    public void placeOrder(PlaceOrderRequest request) {

        if (!marketService.isMarketOpen()) {
            throw new RuntimeException("Order Rejected: Market is closed");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // BUY VALIDATION
        if (request.getType() == OrderType.BUY) {

            double requiredAmount = request.getPrice() * request.getQuantity();

            if (user.getBalance() < requiredAmount) {
                throw new RuntimeException("Insufficient balance for BUY order");
            }
        }

        // SELL VALIDATION
        if (request.getType() == OrderType.SELL) {

            Portfolio portfolio = portfolioRepository
                    .findByUserAndStockSymbol(user, request.getStockSymbol())
                    .orElseThrow(() -> new RuntimeException("No shares available to sell"));

            if (portfolio.getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Insufficient shares to sell");
            }
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .stockSymbol(request.getStockSymbol())
                .type(request.getType())
                .orderMode(request.getOrderMode())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        orderQueue.addOrder(order);

        System.out.println("Order queued → " + order.getId());
    }
}