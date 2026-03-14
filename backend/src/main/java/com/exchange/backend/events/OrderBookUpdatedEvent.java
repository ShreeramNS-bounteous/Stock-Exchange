package com.exchange.backend.events;

import com.exchange.backend.dto.OrderBookResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event fired when orderbook changes.
 */
@Getter
@AllArgsConstructor
public class OrderBookUpdatedEvent {

    private String symbol;
    private OrderBookResponse orderBook;
}