package com.exchange.backend.controller;

import com.exchange.backend.dto.OhlcResponse;
import com.exchange.backend.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/ohlc/{symbol}")
    public OhlcResponse getOhlc(@PathVariable String symbol) {
        return marketService.getOhlc(symbol);
    }

}