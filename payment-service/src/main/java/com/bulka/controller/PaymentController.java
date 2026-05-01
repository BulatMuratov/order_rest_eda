package com.bulka.controller;

import com.bulka.dto.request.PaymentRequest;
import com.bulka.dto.response.PaymentResponse;
import com.bulka.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.pay(paymentRequest));
    }

    @PostMapping("{orderId}/refund")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void releasePay(@PathVariable Long orderId){
        paymentService.refund(orderId);
    }

}
