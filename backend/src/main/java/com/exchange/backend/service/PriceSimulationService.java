package com.exchange.backend.service;

import com.exchange.backend.model.PriceHistory;
import com.exchange.backend.model.Stock;
import com.exchange.backend.repository.PriceHistoryRepository;
import com.exchange.backend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PriceSimulationService {

    private final StockRepository stockRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final MarketService marketService;

    private final Random random = new Random();

    /**
     * Runs every 3 seconds to simulate market price movement
     */
    @Scheduled(fixedRate = 3000)
    public void simulatePriceMovement() {
        // ✅ stop price updates if market closed
        if (!marketService.isMarketOpen()) {
            return;
        }
        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock : stocks) {

            double currentPrice = stock.getPrice();

            // Random change between -2 and +2
            double change = (random.nextDouble() - 0.5) * 4;

            double newPrice = Math.max(1, currentPrice + change);

            stock.setPrice(newPrice);

            stockRepository.save(stock);

            priceHistoryRepository.save(
                    PriceHistory.builder()
                            .stockSymbol(stock.getSymbol())
                            .price(newPrice)
                            .recordedAt(LocalDateTime.now())
                            .build()
            );

            System.out.println(
                    "Market Update → "
                            + stock.getSymbol()
                            + " price updated to ₹"
                            + String.format("%.2f", newPrice)
            );
        }

        System.out.println();
    }
}