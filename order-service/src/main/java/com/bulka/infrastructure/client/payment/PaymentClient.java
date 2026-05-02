package com.bulka.infrastructure.client.payment;

import com.bulka.infrastructure.client.payment.dto.request.PaymentRequest;
import com.bulka.infrastructure.client.payment.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/v1/payments")
    PaymentResponse pay(PaymentRequest paymentRequest);

    @PostMapping("/api/v1/payments/{orderId}/refund")
    void refund(@PathVariable Long orderId);
}
