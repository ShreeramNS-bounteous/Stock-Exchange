package com.exchange.backend.repository;

import com.exchange.backend.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory,Long> {
}
