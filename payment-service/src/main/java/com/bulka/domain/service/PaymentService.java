package com.bulka.domain.service;

import com.bulka.dto.request.PaymentRequest;
import com.bulka.dto.response.PaymentResponse;
import com.bulka.dto.PaymentStatus;
import com.bulka.domain.model.Payment;
import com.bulka.exception.PaymentServiceUnavailableException;
import com.bulka.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

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
            return PaymentResponse.failer("Payment service error. " + e.getMessage());
        }
        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        paymentRepository.save(payment);

        if(payment.getStatus().equals(PaymentStatus.FAILED)) {
            return PaymentResponse.failer("Business error");
        }
        return PaymentResponse.success(payment.getId().toString());
    }

    @Transactional
    public void refund(Long orderId){
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);
    }


    private boolean simulate() {
        double random = Math.random();

        // 80% успех
        if (random < 0.8) {
            sleep(100, 300);
            return true;
        }

        // 18% бизнес-ошибка
        if (random < 0.98) {
            sleep(100, 300);
            return false;
        }

        // 2% техническая ошибка
        sleep(300, 700);
        throw new PaymentServiceUnavailableException("Payment service unavailable");
    }

    private void sleep(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + (int)(Math.random() * (maxMs - minMs)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
