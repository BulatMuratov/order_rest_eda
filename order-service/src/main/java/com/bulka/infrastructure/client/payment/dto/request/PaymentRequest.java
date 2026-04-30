package com.bulka.infrastructure.client.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amount;
}
