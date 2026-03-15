package com.exchange.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandleResponse {

    private long time;

    private double open;

    private double high;

    private double low;

    private double close;
}