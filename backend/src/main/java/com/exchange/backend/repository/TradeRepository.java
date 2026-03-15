package com.exchange.backend.repository;

import com.exchange.backend.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import  java.util.List;

public interface TradeRepository extends JpaRepository<Trade,Long> {
    List<Trade> findByStockSymbolOrderByTimestampDesc(String stockSymbol);
    List<Trade> findByBuyerIdOrSellerId(Long buyerId, Long sellerId);
}
