package com.exchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioResponse {

    private String stockSymbol;
    private int quantity;

}