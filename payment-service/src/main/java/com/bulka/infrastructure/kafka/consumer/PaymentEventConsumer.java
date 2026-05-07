package com.bulka.infrastructure.kafka.consumer;


import com.bulka.domain.service.PaymentService;
import com.bulka.dto.request.PaymentRequest;
import com.bulka.dto.response.PaymentResponse;
import com.bulka.dto.PaymentStatus;
import com.bulka.dto.event.PaymentFailedEvent;
import com.bulka.dto.event.PaymentSuccessEvent;
import com.bulka.dto.event.StartPaymentEvent;
import com.bulka.infrastructure.kafka.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(topics = "orders.commands", groupId = "payment-service")
    public void handleStartPayment(StartPaymentEvent startPaymentEvent) {
        try{
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderId(startPaymentEvent.getOrderId())
                    .amount(startPaymentEvent.getAmount())
                    .build();

            PaymentResponse response = paymentService.pay(paymentRequest);
            if(response.getSuccess()){
                paymentEventProducer.publishPaymentSuccess(PaymentSuccessEvent.builder()
                        .orderId(startPaymentEvent.getOrderId())
                        .transactionId(response.getTransactionId())
                        .reservationId(startPaymentEvent.getReservationId())
                        .build());
            }
            else{
                paymentEventProducer.publishPaymentFailed(PaymentFailedEvent.builder()
                        .orderId(startPaymentEvent.getOrderId())
                        .transactionId(response.getTransactionId())
                        .reservationId(startPaymentEvent.getReservationId())
                        .build());
            }
        }
        catch (Exception e){
            throw e;
        }

    }


}
