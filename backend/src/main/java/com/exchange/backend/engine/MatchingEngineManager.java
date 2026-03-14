package com.exchange.backend.engine;

import com.exchange.backend.model.Order;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MatchingEngineManager {

    private final SymbolOrderRouter router;
    private final MatchingEngine matchingEngine;

    // Keeps track of which symbols already have engines running
    private final Set<String> runningEngines = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void startManager() {

        Thread managerThread = new Thread(() -> {

            while (true) {

                Map<String, BlockingQueue<Order>> queues = router.getAllQueues();

                for (String symbol : queues.keySet()) {

                    // start engine only if not already running
                    if (!runningEngines.contains(symbol)) {

                        startEngine(symbol, queues.get(symbol));
                        runningEngines.add(symbol);
                    }
                }

                try {
                    Thread.sleep(2000); // check every 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        });

        managerThread.setName("MatchingEngineManager");
        managerThread.start();
    }

    private void startEngine(String symbol, BlockingQueue<Order> queue) {

        Thread engineThread = new Thread(() -> {

            System.out.println("🚀 Starting Matching Engine for " + symbol);

            while (true) {

                try {

                    Order order = queue.take(); // waits until order arrives

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " processing order "
                                    + order.getId()
                    );

                    matchingEngine.processOrder(order);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }

        });

        engineThread.setName("Engine-" + symbol);
        engineThread.start();
    }
}