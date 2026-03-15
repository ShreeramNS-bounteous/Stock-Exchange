package com.exchange.backend.service;

import com.exchange.backend.dto.CandleResponse;
import com.exchange.backend.dto.OhlcResponse;
import com.exchange.backend.model.PriceHistory;
import com.exchange.backend.repository.PriceHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
@AllArgsConstructor
public class MarketService {

    private final PriceHistoryRepository priceHistoryRepository;

    // Market open for entire day (for development/testing)
    private static final LocalTime MARKET_OPEN = LocalTime.of(0, 0);
    private static final LocalTime MARKET_CLOSE = LocalTime.of(23, 59);

    public boolean isMarketOpen() {

        LocalTime now = LocalTime.now();

        return !now.isBefore(MARKET_OPEN) && !now.isAfter(MARKET_CLOSE);
    }

    public OhlcResponse getOhlc(String symbol) {

        List<PriceHistory> prices =
                priceHistoryRepository.findByStockSymbolOrderByRecordedAtAsc(symbol);

        if (prices.isEmpty()) {
            throw new RuntimeException("No price data found for " + symbol);
        }

        double open = prices.get(0).getPrice();
        double close = prices.get(prices.size() - 1).getPrice();

        double high = prices.stream()
                .mapToDouble(PriceHistory::getPrice)
                .max()
                .orElse(open);

        double low = prices.stream()
                .mapToDouble(PriceHistory::getPrice)
                .min()
                .orElse(open);

        return new OhlcResponse(symbol, open, high, low, close);
    }

    public List<CandleResponse> getCandles(String symbol){

        List<PriceHistory> prices =
                priceHistoryRepository
                        .findByStockSymbolOrderByRecordedAtAsc(symbol);

        return prices.stream()
                .map(p -> new CandleResponse(
                        p.getRecordedAt().toEpochSecond(java.time.ZoneOffset.UTC),
                        p.getPrice(),
                        p.getPrice(),
                        p.getPrice(),
                        p.getPrice()
                ))
                .toList();
    }

}