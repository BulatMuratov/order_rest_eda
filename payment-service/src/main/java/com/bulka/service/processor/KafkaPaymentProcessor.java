package com.bulka.service.processor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class KafkaPaymentProcessor implements PaymentProcessor {
}
