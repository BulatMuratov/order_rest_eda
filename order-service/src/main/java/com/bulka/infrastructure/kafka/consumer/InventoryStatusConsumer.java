package com.bulka.infrastructure.kafka.consumer;

import com.bulka.application.impl.KafkaOrderWorkflow;
import com.bulka.dto.OrderItemFilled;
import com.bulka.dto.event.StockFailedEvent;
import com.bulka.dto.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@KafkaListener(id = "order-inventory-group", topics = "inventory.status", groupId = "order-service")
public class InventoryStatusConsumer {
    private final KafkaOrderWorkflow orderWorkflow;

    @KafkaHandler
    public void handleStockReserved(StockReservedEvent event) {
        List<OrderItemFilled> itemList = event.getItems().stream()
                .map(item ->
                        new OrderItemFilled(item.getProductId(), item.getQuantity(), item.getPrice()))
                .toList();

        orderWorkflow.confirmReservation(event.getOrderId(), event.getReservationId(), itemList);
    }

    @KafkaHandler
    public void handleStockReserveFailed(StockFailedEvent event) {

    }
}
