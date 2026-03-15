package com.exchange.backend.controller;

import com.exchange.backend.dto.CandleResponse;
import com.exchange.backend.dto.MarketStatusResponse;
import com.exchange.backend.dto.OhlcResponse;
import com.exchange.backend.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/ohlc/{symbol}")
    public OhlcResponse getOhlc(@PathVariable String symbol) {
        return marketService.getOhlc(symbol);
    }

    @GetMapping("/status")
    public MarketStatusResponse marketStatus(){

        return new MarketStatusResponse(
                marketService.isMarketOpen()
        );
    }

    @GetMapping("/candles/{symbol}")
    public List<CandleResponse> getCandles(
            @PathVariable String symbol){

        return marketService.getCandles(symbol);
    }

}