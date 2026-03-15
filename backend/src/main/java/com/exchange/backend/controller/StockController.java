package com.exchange.backend.controller;

import com.exchange.backend.model.Stock;
import com.exchange.backend.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;

    @GetMapping
    public List<Stock> getAllStocks(){
        return stockRepository.findAll();
    }
}