package com.exchange.backend.dto;

import com.exchange.backend.enums.OrderMode;
import com.exchange.backend.enums.OrderType;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    private Long userId;

    private String stockSymbol;

    private OrderType type;

    private OrderMode orderMode;

    private int quantity;

    private double price;
}