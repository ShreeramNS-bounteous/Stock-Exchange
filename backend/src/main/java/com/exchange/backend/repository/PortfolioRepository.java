package com.exchange.backend.repository;

import com.exchange.backend.model.Portfolio;
import com.exchange.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    Optional<Portfolio> findByUserAndStockSymbol(User user, String stockSymbol);
    List<Portfolio> findByUserId(Long userId);
}
