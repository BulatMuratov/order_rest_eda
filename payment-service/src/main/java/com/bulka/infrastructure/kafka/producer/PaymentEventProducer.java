package com.bulka.infrastructure.kafka.producer;

import com.bulka.dto.event.PaymentFailedEvent;
import com.bulka.dto.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentSuccess(PaymentSuccessEvent event){
        kafkaTemplate.send("payments.lifecycle", event.getOrderId().toString(), event);
    }

    public void publishPaymentFailed(PaymentFailedEvent event){
        kafkaTemplate.send("payments.lifecycle", event.getOrderId().toString(), event);
    }
}
