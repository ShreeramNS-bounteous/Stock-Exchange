package com.exchange.backend.service;

import com.exchange.backend.dto.PortfolioResponse;
import com.exchange.backend.model.Portfolio;
import com.exchange.backend.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    /**
     * Returns portfolio holdings for a user.
     */
    public List<PortfolioResponse> getUserPortfolio(Long userId) {

        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);

        return portfolios.stream()
                .map(p -> new PortfolioResponse(
                        p.getStockSymbol(),
                        p.getQuantity()
                ))
                .toList();
    }
}