package com.exchange.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;


@Service
public class MarketService {

    // Market open for entire day (for development/testing)
    private static final LocalTime MARKET_OPEN = LocalTime.of(0, 0);
    private static final LocalTime MARKET_CLOSE = LocalTime.of(23, 59);

    public boolean isMarketOpen() {

        LocalTime now = LocalTime.now();

        return !now.isBefore(MARKET_OPEN) && !now.isAfter(MARKET_CLOSE);
    }

}