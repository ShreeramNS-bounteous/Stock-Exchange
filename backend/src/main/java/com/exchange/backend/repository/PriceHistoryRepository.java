package com.exchange.backend.repository;

import com.exchange.backend.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory,Long> {
    List<PriceHistory> findByStockSymbolOrderByRecordedAtAsc(String stockSymbol);

}
