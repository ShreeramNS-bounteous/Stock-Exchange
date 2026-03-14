package com.exchange.backend.engine;

import com.exchange.backend.enums.OrderStatus;
import com.exchange.backend.enums.OrderType;
import com.exchange.backend.enums.TransactionType;
import com.exchange.backend.model.*;
import com.exchange.backend.repository.PortfolioRepository;
import com.exchange.backend.repository.TradeRepository;
import com.exchange.backend.repository.TransactionRepository;
import com.exchange.backend.repository.UserRepository;
import com.exchange.backend.service.MarketService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MatchingEngine {

    private final OrderQueue orderQueue;
    private final OrderBook orderBook;
    private final TradeRepository tradeRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final MarketService marketService;

    @PostConstruct
    public void startEngine() {

        Thread engineThread = new Thread(() -> {

            while (true) {

                Order order = orderQueue.pollOrder();

                if (order != null) {
                    System.out.println("ENGINE RECEIVED ORDER → " + order.getType() + " " + order.getStockSymbol());
                    processOrder(order);
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {}
            }

        });

        engineThread.setName("Matching-Engine");
        engineThread.start();
    }

    private void processOrder(Order order) {

        if (order.getType() == OrderType.BUY) {
            matchBuyOrder(order);
        } else {
            matchSellOrder(order);
        }

        System.out.println("BUY BOOK SIZE → " + orderBook.getBuyOrders().size());
        System.out.println("SELL BOOK SIZE → " + orderBook.getSellOrders().size());
    }

    private void matchBuyOrder(Order buyOrder) {

        while (!orderBook.getSellOrders().isEmpty()) {

            Order sellOrder = orderBook.getSellOrders().peek();

            if (buyOrder.getPrice() < sellOrder.getPrice()) {
                break;
            }

            executeTrade(buyOrder, sellOrder);

            if (sellOrder.getQuantity() == 0) {
                orderBook.getSellOrders().poll();
            }

            if (buyOrder.getQuantity() == 0) {
                return;
            }
        }

        orderBook.addOrder(buyOrder);
    }

    private void matchSellOrder(Order sellOrder) {

        while (!orderBook.getBuyOrders().isEmpty()) {

            Order buyOrder = orderBook.getBuyOrders().peek();

            if (buyOrder.getPrice() < sellOrder.getPrice()) {
                break;
            }

            executeTrade(buyOrder, sellOrder);

            if (buyOrder.getQuantity() == 0) {
                orderBook.getBuyOrders().poll();
            }

            if (sellOrder.getQuantity() == 0) {
                return;
            }
        }

        orderBook.addOrder(sellOrder);
    }

    private void executeTrade(Order buyOrder, Order sellOrder) {

        int tradeQty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
        double price = sellOrder.getPrice();

        buyOrder.setQuantity(buyOrder.getQuantity() - tradeQty);
        sellOrder.setQuantity(sellOrder.getQuantity() - tradeQty);

        String stock = buyOrder.getStockSymbol();
        double amount = tradeQty * price;

        // Fetch Users
        User buyer = userRepository.findById(buyOrder.getUserId()).orElseThrow();
        User seller = userRepository.findById(sellOrder.getUserId()).orElseThrow();

        System.out.println(
                "TRADE EXECUTED → "
                        + tradeQty + " "
                        + stock
                        + " @ " + price
                        + " Buyer:" + buyer.getId()
                        + " Seller:" + seller.getId()
        );

        // Save Trade
        Trade trade = Trade.builder()
                .buyer(buyer)
                .seller(seller)
                .stockSymbol(stock)
                .quantity(tradeQty)
                .price(price)
                .timestamp(LocalDateTime.now())
                .build();

        tradeRepository.save(trade);

        // Update balances
        buyer.setBalance(buyer.getBalance() - amount);
        seller.setBalance(seller.getBalance() + amount);

        userRepository.save(buyer);
        userRepository.save(seller);

        // Update portfolio
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

        Portfolio sellerPortfolio = portfolioRepository
                .findByUserAndStockSymbol(seller, stock)
                .orElseThrow();

        sellerPortfolio.setQuantity(sellerPortfolio.getQuantity() - tradeQty);
        portfolioRepository.save(sellerPortfolio);

        // Save buyer transaction
        transactionRepository.save(
                Transaction.builder()
                        .user(buyer)
                        .amount(amount)
                        .type(TransactionType.TRADE_DEBIT)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // Save seller transaction
        transactionRepository.save(
                Transaction.builder()
                        .user(seller)
                        .amount(amount)
                        .type(TransactionType.TRADE_CREDIT)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        if (buyOrder.getQuantity() == 0)
            buyOrder.setStatus(OrderStatus.EXECUTED);

        if (sellOrder.getQuantity() == 0)
            sellOrder.setStatus(OrderStatus.EXECUTED);
    }
}