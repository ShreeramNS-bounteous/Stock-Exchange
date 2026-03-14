package com.exchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderBookResponse {

    private List<OrderLevel> buyOrders;
    private List<OrderLevel> sellOrders;

}