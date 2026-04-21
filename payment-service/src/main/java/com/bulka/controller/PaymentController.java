package com.bulka.controller;

import com.bulka.dto.PaymentRequest;
import com.bulka.dto.PaymentResponse;
import com.bulka.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> process(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.pay(paymentRequest));
    }

}
