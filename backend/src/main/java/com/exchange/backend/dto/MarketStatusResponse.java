package com.exchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarketStatusResponse {

    private boolean open;
}