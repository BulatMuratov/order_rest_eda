package com.bulka.infrastructure.kafka.producer;

import com.bulka.dto.event.ConfirmReservedEvent;
import com.bulka.dto.event.OrderCreatedEvent;
import com.bulka.dto.event.StartPaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event){
        kafkaTemplate.send("orders.lifecycle", event.getOrderId().toString(), event);
    }

    public void publishStartPayment(StartPaymentEvent event){
        kafkaTemplate.send("orders.commands", event.getOrderId().toString(), event);
    }

    public void publishConfirmStock(ConfirmReservedEvent event){
        kafkaTemplate.send("inventory.commands", event.getReservationId().toString(), event);
    }
}
