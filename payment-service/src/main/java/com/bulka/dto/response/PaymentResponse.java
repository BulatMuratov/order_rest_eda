package com.bulka.dto.response;

import com.bulka.dto.PaymentStatus;
import com.bulka.dto.request.PaymentRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String transactionId;
    private Boolean success;
    private String errorMessage;

    public static PaymentResponse success(String transactionId) {
        return PaymentResponse.builder()
                .transactionId(transactionId)
                .success(true)
                .build();
    }

    public static PaymentResponse failer(String errorMessage) {
        return PaymentResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }


}
