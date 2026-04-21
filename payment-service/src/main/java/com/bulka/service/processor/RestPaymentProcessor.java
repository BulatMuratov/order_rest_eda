package com.bulka.service.processor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("rest")
public class RestPaymentProcessor implements PaymentProcessor {
}
