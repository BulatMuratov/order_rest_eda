package com.bulka.infrastructure.kafka.producer;

import com.bulka.dto.event.StockFailedEvent;
import com.bulka.dto.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishStockReserved(StockReservedEvent event){
        kafkaTemplate.send("inventory.status", event.getReservationId().toString(), event);
    }

    public void publishStockFailed(StockFailedEvent event){
        kafkaTemplate.send("inventory.status", event.getOrderId().toString(), event);
    }
}
