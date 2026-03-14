package com.exchange.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class MarketService {

    private static final LocalTime MARKET_OPEN = LocalTime.of(9, 15);
    private static final LocalTime MARKET_CLOSE = LocalTime.of(15, 30);

    public boolean isMarketOpen() {

        LocalTime now = LocalTime.now();

        return !now.isBefore(MARKET_OPEN) && !now.isAfter(MARKET_CLOSE);
    }

}