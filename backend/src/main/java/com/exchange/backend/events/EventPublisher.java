package com.exchange.backend.events;

import com.exchange.backend.dto.OrderBookResponse;
import com.exchange.backend.dto.TradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Responsible for publishing domain events
 * like TradeExecutedEvent and OrderBookUpdatedEvent.
 */
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publish trade executed event
     */
    public void publishTradeExecuted(TradeResponse trade) {

        TradeExecutedEvent event = new TradeExecutedEvent(trade);

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publish orderbook updated event
     */
    public void publishOrderBookUpdated(String symbol, OrderBookResponse orderBook) {

        OrderBookUpdatedEvent event =
                new OrderBookUpdatedEvent(symbol, orderBook);

        applicationEventPublisher.publishEvent(event);
    }
}