package com.exchange.backend.config;

import com.exchange.backend.model.Portfolio;
import com.exchange.backend.model.Stock;
import com.exchange.backend.model.User;
import com.exchange.backend.repository.PortfolioRepository;
import com.exchange.backend.repository.StockRepository;
import com.exchange.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final PortfolioRepository portfolioRepository;

    @PostConstruct
    public void init() {

        System.out.println("🌱 Seeding Database...");

        // USERS
        User u1 = userRepository.save(
                User.builder()
                        .name("MarketMaker1")
                        .email("maker1@exchange.com")
                        .password("123")
                        .balance(1_000_000.0)
                        .build()
        );

        User u2 = userRepository.save(
                User.builder()
                        .name("MarketMaker2")
                        .email("maker2@exchange.com")
                        .password("123")
                        .balance(1_000_000.0)
                        .build()
        );

        // STOCKS
        stockRepository.save(Stock.builder().symbol("TCS").build());
        stockRepository.save(Stock.builder().symbol("INFY").build());
        stockRepository.save(Stock.builder().symbol("RELIANCE").build());
        stockRepository.save(Stock.builder().symbol("HDFC").build());
        stockRepository.save(Stock.builder().symbol("ITC").build());

        portfolioRepository.save(
                Portfolio.builder()
                        .user(u1)
                        .stockSymbol("TCS")
                        .quantity(200)
                        .build()
        );

        portfolioRepository.save(
                Portfolio.builder()
                        .user(u1)
                        .stockSymbol("INFY")
                        .quantity(200)
                        .build()
        );

        portfolioRepository.save(
                Portfolio.builder()
                        .user(u2)
                        .stockSymbol("RELIANCE")
                        .quantity(200)
                        .build()
        );

        portfolioRepository.save(
                Portfolio.builder()
                        .user(u2)
                        .stockSymbol("HDFC")
                        .quantity(200)
                        .build()
        );

        System.out.println("✅ Database Seeded");
    }
}