package com.exchange.backend.service;

import com.exchange.backend.dto.PlaceOrderRequest;
import com.exchange.backend.enums.OrderMode;
import com.exchange.backend.enums.OrderType;
import com.exchange.backend.model.Stock;
import com.exchange.backend.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class OrderSimulatorService {

    private final OrderService orderService;
    private final StockRepository stockRepository;

    // thread safe map
    private final Map<String, Double> lastPrice = new ConcurrentHashMap<>();

    private final Random random = new Random();

    private List<String> symbols;

    // simulator accounts
    private final Long[] simulatorUsers = {1L, 2L};

    private ExecutorService executor;

    private volatile boolean running = true;

    @PostConstruct
    public void startSimulation() {

        System.out.println("🚀 Trading Simulator Started");

        symbols = stockRepository.findAll()
                .stream()
                .map(Stock::getSymbol)
                .toList();

        if (symbols.isEmpty()) {
            throw new RuntimeException("No stocks found in database");
        }

        // initialize base prices
        symbols.forEach(symbol -> lastPrice.put(symbol, 3400.0));

        executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {

            executor.submit(() -> {

                while (running) {

                    try {

                        PlaceOrderRequest request = generateRandomOrder();

                        Long simulatorUser =
                                simulatorUsers[random.nextInt(simulatorUsers.length)];

                        orderService.placeOrder(simulatorUser, request);

                        Thread.sleep(2000 + random.nextInt(2000));

                    } catch (Exception ignored) {
                        // ignore validation errors
                    }

                }

            });

        }

    }

    @PreDestroy
    public void stopSimulator() {

        System.out.println("🛑 Stopping Trading Simulator");

        running = false;

        if (executor != null) {
            executor.shutdownNow();
        }

    }

    private PlaceOrderRequest generateRandomOrder() {

        String symbol =
                symbols.get(random.nextInt(symbols.size()));

        OrderType type =
                random.nextBoolean() ? OrderType.BUY : OrderType.SELL;

        int quantity =
                random.nextInt(5) + 1;

        // get last price
        double previousPrice = lastPrice.get(symbol);

        // small movement
        double movement =
                (random.nextDouble() - 0.5) * 6;

        double newPrice =
                Math.max(100, previousPrice + movement);

        // store updated price
        lastPrice.put(symbol, newPrice);

        PlaceOrderRequest request = new PlaceOrderRequest();

        request.setStockSymbol(symbol);
        request.setType(type);
        request.setOrderMode(OrderMode.LIMIT);
        request.setQuantity(quantity);
        request.setPrice(newPrice);

        return request;
    }
}