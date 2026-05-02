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
        kafkaTemplate.send("order-events", event.getOrderId().toString(), event);
    }

    public void publishStartPayment(StartPaymentEvent event){
        kafkaTemplate.send("order-events", event.getOrderId().toString(), event);
    }

    public void publishConfirmStock(ConfirmReservedEvent event){
        kafkaTemplate.send("order-events", event.getReservationId().toString(), event);
    }
}
