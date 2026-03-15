package com.exchange.backend.engine;

import com.exchange.backend.enums.OrderMode;
import com.exchange.backend.enums.OrderStatus;
import com.exchange.backend.enums.OrderType;
import com.exchange.backend.enums.TransactionType;
import com.exchange.backend.events.EventPublisher;
import com.exchange.backend.model.*;
import com.exchange.backend.repository.*;
import com.exchange.backend.dto.TradeResponse;
import com.exchange.backend.dto.OrderBookResponse;


import com.exchange.backend.service.OrderBookService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class MatchingEngine {

    private final OrderBook orderBook;
    private final TradeRepository tradeRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;
    private final OrderBookService orderBookService;
    private static final Logger log = LoggerFactory.getLogger(MatchingEngine.class);

    /**
     * Rebuild in-memory orderbook from DB on startup
     */
    @PostConstruct
    public void rebuildOrderBook() {

        System.out.println("Rebuilding OrderBook from DB...");

        orderRepository.findAll()
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .forEach(orderBook::addOrder);

        System.out.println("OrderBook restored from database.");
    }

    /**
     * Entry point called by MatchingEngineManager
     */
    public void processOrder(Order order) {

        if (order.getType() == OrderType.BUY) {
            matchBuyOrder(order);
        } else {
            matchSellOrder(order);
        }

//        System.out.println("BUY BOOK SIZE → " + orderBook.getBuyOrders(order.getStockSymbol()).size());
//        System.out.println("SELL BOOK SIZE → " + orderBook.getSellOrders(order.getStockSymbol()).size());
//
//        orderBook.printOrderBook(order.getStockSymbol());

        /**
         * Broadcast updated orderbook
         */
        publishOrderBookUpdate(order.getStockSymbol());
    }

    /**
     * BUY order matching
     */
    private void matchBuyOrder(Order buyOrder) {

        var sellQueue = orderBook.getSellOrders(buyOrder.getStockSymbol());

        while (!sellQueue.isEmpty()) {

            Order sellOrder = sellQueue.peek();

            if (buyOrder.getOrderMode() == OrderMode.LIMIT &&
                    buyOrder.getPrice() < sellOrder.getPrice()) {
                break;
            }

            executeTrade(buyOrder, sellOrder);

            if (sellOrder.getQuantity() == 0) {
                sellQueue.poll();
            }

            if (buyOrder.getQuantity() == 0) {
                return;
            }
        }

        orderBook.addOrder(buyOrder);
    }

    /**
     * SELL order matching
     */
    private void matchSellOrder(Order sellOrder) {

        var buyQueue = orderBook.getBuyOrders(sellOrder.getStockSymbol());

        while (!buyQueue.isEmpty()) {

            Order buyOrder = buyQueue.peek();

            if (sellOrder.getOrderMode() == OrderMode.LIMIT &&
                    buyOrder.getPrice() < sellOrder.getPrice()) {
                break;
            }

            executeTrade(buyOrder, sellOrder);

            if (buyOrder.getQuantity() == 0) {
                buyQueue.poll();
            }

            if (sellOrder.getQuantity() == 0) {
                return;
            }
        }

        orderBook.addOrder(sellOrder);
    }

    /**
     * Trade execution logic
     */
    private void executeTrade(Order buyOrder, Order sellOrder) {

        if (buyOrder.getUserId().equals(sellOrder.getUserId())) {

            System.out.println("Self trade prevented");

            // remove SELL order from book to break deadlock
            orderBook.getSellOrders(sellOrder.getStockSymbol()).poll();

            sellOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(sellOrder);

            return;
        }

        int tradeQty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

        double price;

// MARKET BUY → take best SELL price
        if (buyOrder.getOrderMode() == OrderMode.MARKET) {
            price = sellOrder.getPrice();
        }

// MARKET SELL → take best BUY price
        else if (sellOrder.getOrderMode() == OrderMode.MARKET) {
            price = buyOrder.getPrice();
        }

// LIMIT vs LIMIT → resting order price
        else {
            price = sellOrder.getPrice();
        }

        buyOrder.setQuantity(buyOrder.getQuantity() - tradeQty);
        sellOrder.setQuantity(sellOrder.getQuantity() - tradeQty);

        String stock = buyOrder.getStockSymbol();
        double amount = tradeQty * price;

        User buyer = userRepository.findById(buyOrder.getUserId()).orElseThrow();
        User seller = userRepository.findById(sellOrder.getUserId()).orElseThrow();

        // RISK CHECK
        if (buyer.getBalance() < amount) {
            System.out.println("Trade rejected: Buyer insufficient balance");
            return;
        }

        Portfolio sellerPortfolio = portfolioRepository
                .findByUserAndStockSymbol(seller, buyOrder.getStockSymbol())
                .orElse(null);

        if (sellerPortfolio == null || sellerPortfolio.getQuantity() < tradeQty) {
            System.out.println("Trade rejected: Seller insufficient shares");
            return;
        }

      log.info("TRADE EXECUTED → {} {} @ {}", tradeQty, stock, price);

        Trade trade = Trade.builder()
                .buyer(buyer)
                .seller(seller)
                .stockSymbol(stock)
                .quantity(tradeQty)
                .price(price)
                .timestamp(LocalDateTime.now())
                .build();

        tradeRepository.save(trade);

        /**
         * Convert Trade → DTO for WebSocket
         */
        TradeResponse tradeResponse = new TradeResponse(
                trade.getId(),
                trade.getStockSymbol(),
                trade.getQuantity(),
                trade.getPrice(),
                buyer.getId(),
                seller.getId(),
                trade.getTimestamp()
        );

        /**
         * Broadcast trade event
         */
        eventPublisher.publishTradeExecuted(tradeResponse);

        /**
         * Update balances
         */
        seller.setBalance(seller.getBalance() + amount);

        userRepository.save(buyer);
        userRepository.save(seller);

        /**
         * Update buyer portfolio
         */
        Portfolio portfolio = portfolioRepository
                .findByUserAndStockSymbol(buyer, stock)
                .orElse(
                        Portfolio.builder()
                                .user(buyer)
                                .stockSymbol(stock)
                                .quantity(0)
                                .build()
                );

        portfolio.setQuantity(portfolio.getQuantity() + tradeQty);
        portfolioRepository.save(portfolio);

        /**
         * Record transactions
         */
        transactionRepository.save(
                Transaction.builder()
                        .user(buyer)
                        .amount(amount)
                        .type(TransactionType.TRADE_DEBIT)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        transactionRepository.save(
                Transaction.builder()
                        .user(seller)
                        .amount(amount)
                        .type(TransactionType.TRADE_CREDIT)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        /**
         * Update order status
         */
        if (buyOrder.getQuantity() == 0)
            buyOrder.setStatus(OrderStatus.EXECUTED);

        if (sellOrder.getQuantity() == 0)
            sellOrder.setStatus(OrderStatus.EXECUTED);

        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);
    }

    /**
     * Publish orderbook updates to WebSocket
     */
    private void publishOrderBookUpdate(String symbol) {

        OrderBookResponse response = orderBookService.getOrderBook(symbol);

        eventPublisher.publishOrderBookUpdated(symbol, response);
    }
}