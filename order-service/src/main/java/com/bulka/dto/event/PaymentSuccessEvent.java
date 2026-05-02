package com.bulka.dto.event;

import com.bulka.dto.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSuccessEvent {
    private String transactionId;
    private Long reservationId;
    private PaymentStatus paymentStatus;
}
