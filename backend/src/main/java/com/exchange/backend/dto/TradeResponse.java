package com.exchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO returned to clients for executed trades
 */
@Data
@AllArgsConstructor
public class TradeResponse {

    private Long tradeId;
    private String stockSymbol;
    private int quantity;
    private double price;
    private Long buyerId;
    private Long sellerId;
    private LocalDateTime timestamp;
}