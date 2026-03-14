package com.exchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderLevel {

    private double price;
    private int quantity;

}