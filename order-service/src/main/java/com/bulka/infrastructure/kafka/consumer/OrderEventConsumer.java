package com.bulka.infrastructure.kafka.consumer;

import com.bulka.application.OrderWorkflow;
import com.bulka.dto.event.StockFailedEvent;
import com.bulka.dto.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderWorkflow orderWorkflow;

    @KafkaListener(topics = "inventory-events", groupId = "order-service")
    public void handleStockReserved(StockReservedEvent event) {

    }

    @KafkaListener(topics = "inventory-events", groupId = "order-service")
    public void handleStockReserveFailed(StockFailedEvent event) {

    }

}