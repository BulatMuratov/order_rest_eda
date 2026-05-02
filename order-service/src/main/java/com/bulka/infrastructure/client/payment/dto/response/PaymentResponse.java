package com.bulka.infrastructure.client.payment.dto.response;

import com.bulka.infrastructure.client.payment.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponse {
    private String transactionId;
    private Boolean success;
    private String errorMessage;
}
