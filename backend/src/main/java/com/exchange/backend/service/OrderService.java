package com.exchange.backend.service;

import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.engine.OrderBook;

import com.exchange.backend.engine.SymbolOrderRouter;
import com.exchange.backend.enums.OrderMode;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SymbolOrderRouter symbolOrderRouter;
    private final MarketService marketService;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final OrderBook orderBook;

    public void placeOrder(Long userId, PlaceOrderRequest request) {

        if (!marketService.isMarketOpen()) {
            throw new RuntimeException("Order Rejected: Market is closed");
        }

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // BUY VALIDATION
        if (request.getType() == OrderType.BUY && request.getOrderMode() == OrderMode.LIMIT) {

            double requiredAmount = request.getPrice() * request.getQuantity();

            if (user.getBalance() < requiredAmount) {
                throw new RuntimeException("Insufficient balance for BUY order");
            }

            user.setBalance(user.getBalance() - requiredAmount);
            userRepository.save(user);
        }

        // SELL VALIDATION
        if (request.getType() == OrderType.SELL) {

            Portfolio portfolio = portfolioRepository
                    .findByUserAndStockSymbol(user, request.getStockSymbol())
                    .orElseThrow(() -> new RuntimeException("No shares available to sell"));

            if (portfolio.getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Insufficient shares to sell");
            }

            portfolio.setQuantity(portfolio.getQuantity() - request.getQuantity());
            portfolioRepository.save(portfolio);
        }

        if (request.getOrderMode() == OrderMode.MARKET) {
            request.setPrice(0.0);
        }

        Order order = Order.builder()
                .userId(userId)
                .stockSymbol(request.getStockSymbol())
                .type(request.getType())
                .orderMode(request.getOrderMode())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        symbolOrderRouter.routeOrder(order);

        System.out.println("Order queued → " + order.getId());
    }

    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }

        orderBook.removeOrder(order);

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + order.getUserId()));

        // refund money if BUY order
        if(order.getType() == OrderType.BUY){

            double refund = order.getPrice() * order.getQuantity();

            user.setBalance(user.getBalance() + refund);

            userRepository.save(user);
        }

        // return shares if SELL order
        if(order.getType() == OrderType.SELL){

            Portfolio portfolio = portfolioRepository
                    .findByUserAndStockSymbol(user, order.getStockSymbol())
                    .orElse(null);

            if (portfolio == null) {

                portfolio = Portfolio.builder()
                        .user(user)
                        .stockSymbol(order.getStockSymbol())
                        .quantity(order.getQuantity())
                        .build();

            } else {

                portfolio.setQuantity(portfolio.getQuantity() + order.getQuantity());
            }

            portfolioRepository.save(portfolio);
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    public List<Order> getOpenOrders(Long userId){

        return orderRepository.findByUserIdAndStatus(
                userId,
                OrderStatus.PENDING
        );
    }

    public List<Order> getOrderHistory(Long userId){

        return orderRepository.findByUserId(userId);
    }
}