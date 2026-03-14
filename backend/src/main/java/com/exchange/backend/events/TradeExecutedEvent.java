package com.exchange.backend.events;

import com.exchange.backend.dto.TradeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event fired when a trade is executed.
 */
@Getter
@AllArgsConstructor
public class TradeExecutedEvent {

    private TradeResponse trade;
}