package com.bulka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String transactionId;
    private PaymentStatus paymentStatus;
}
