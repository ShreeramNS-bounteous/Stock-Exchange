package com.exchange.backend.websocket;

import com.exchange.backend.events.OrderBookUpdatedEvent;
import com.exchange.backend.events.TradeExecutedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Listens for domain events and broadcasts them
 * to WebSocket subscribers.
 */
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Sends trade updates to subscribers
     */
    @EventListener
    public void handleTradeExecuted(TradeExecutedEvent event) {

        String symbol = event.getTrade().getStockSymbol();

        messagingTemplate.convertAndSend(
                "/topic/trades/" + symbol,
                event.getTrade()
        );
    }

    /**
     * Sends orderbook updates to subscribers
     */
    @EventListener
    public void handleOrderBookUpdate(OrderBookUpdatedEvent event) {

        messagingTemplate.convertAndSend(
                "/topic/orderbook/" + event.getSymbol(),
                event.getOrderBook()
        );
    }
}