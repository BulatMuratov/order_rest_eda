package com.bulka.infrastructure.kafka.consumer;

import com.bulka.application.impl.KafkaOrderWorkflow;
import com.bulka.dto.event.PaymentFailedEvent;
import com.bulka.dto.event.PaymentSuccessEvent;
import com.bulka.infrastructure.kafka.producer.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(id = "order-payment-group", topics = "payments.lifecycle", groupId = "order-service")
public class PaymentLifecycleConsumer {

    private final KafkaOrderWorkflow orderWorkflow;


    @KafkaHandler
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        orderWorkflow.finishOrder(event.getOrderId(), event.getReservationId());
    }

    @KafkaHandler
    public void handlePaymentFailed(PaymentFailedEvent event) {
        orderWorkflow.processPaymentFailure(event.getOrderId(), event.getReservationId());
    }

}
