package com.bulka.service;

import com.bulka.dto.PaymentRequest;
import com.bulka.dto.PaymentResponse;
import com.bulka.dto.PaymentStatus;
import com.bulka.model.Payment;
import com.bulka.repository.PaymentRepository;
import com.bulka.service.processor.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentProcessor paymentProcessor;

    private final PaymentRepository paymentRepository;

    public PaymentResponse pay(PaymentRequest paymentRequest) {
        Payment payment = Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        boolean success;
        try {
            success = simulate();
        }
        catch (Exception e) {
            success = false;
        }
        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        paymentRepository.save(payment);


        return PaymentResponse.builder()
                .transactionId(payment.getId().toString())
                .paymentStatus(payment.getStatus())
                .build();
    }

    private boolean simulate() {
        double random = Math.random();

        // 70% успех
        if (random < 0.7) {
            sleep(100, 300);
            return true;
        }

        // 20% бизнес-ошибка
        if (random < 0.9) {
            sleep(100, 300);
            return false;
        }

        // 10% техническая ошибка
        sleep(300, 700);
        throw new RuntimeException("Payment service unavailable");
    }

    private void sleep(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + (int)(Math.random() * (maxMs - minMs)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
