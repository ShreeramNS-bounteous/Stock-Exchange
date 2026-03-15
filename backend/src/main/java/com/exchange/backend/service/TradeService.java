package com.exchange.backend.service;

import com.exchange.backend.dto.TradeResponse;
import com.exchange.backend.model.Trade;
import com.exchange.backend.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    public List<TradeResponse> getAllTrades() {

        List<Trade> trades = tradeRepository.findAll();

        return trades.stream()
                .map(t -> new TradeResponse(
                        t.getId(),
                        t.getStockSymbol(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getBuyer().getId(),
                        t.getSeller().getId(),
                        t.getTimestamp()
                ))
                .toList();
    }

    public List<TradeResponse> getTradesBySymbol(String symbol) {

        List<Trade> trades =
                tradeRepository.findByStockSymbolOrderByTimestampDesc(symbol);

        return trades.stream()
                .map(t -> new TradeResponse(
                        t.getId(),
                        t.getStockSymbol(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getBuyer().getId(),
                        t.getSeller().getId(),
                        t.getTimestamp()
                ))
                .toList();
    }

    public List<TradeResponse> getUserTrades(Long userId){

        List<Trade> trades =
                tradeRepository.findByBuyerIdOrSellerId(userId, userId);

        return trades.stream()
                .map(t -> new TradeResponse(
                        t.getId(),
                        t.getStockSymbol(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getBuyer().getId(),
                        t.getSeller().getId(),
                        t.getTimestamp()
                ))
                .toList();
    }
}