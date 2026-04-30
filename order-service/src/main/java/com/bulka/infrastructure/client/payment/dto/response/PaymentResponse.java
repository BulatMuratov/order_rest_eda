package com.bulka.infrastructure.client.payment;

import lombok.Data;

@Data
public class PaymentResponse {
    private String transactionId;
    private PaymentStatus paymentStatus;
}
