package com.bulka.infrastructure.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    @KafkaListener(topics = "inventory-events", groupId = "order-service")
    public void listen(Object event) {
        // convert + delegate to application layer
    }
}