//package com.bulka.infrastructure.kafka.consumer;
//
//import com.bulka.application.impl.KafkaOrderWorkflow;
//import com.bulka.dto.OrderItemFilled;
//import com.bulka.dto.event.ConfirmReservedEvent;
//import com.bulka.dto.event.PaymentFailedEvent;
//import com.bulka.dto.event.PaymentSuccessEvent;
//import com.bulka.dto.event.StartPaymentEvent;
//import com.bulka.dto.event.StockFailedEvent;
//import com.bulka.dto.event.StockReservedEvent;
//import com.bulka.infrastructure.kafka.producer.OrderEventProducer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class OrderEventConsumer {
//
//    private final KafkaOrderWorkflow orderWorkflow;
//    private final OrderEventProducer orderEventProducer;
//
//    @KafkaListener(topics = "inventory.status", groupId = "order-service")
//    public void handleStockReserved(StockReservedEvent event) {
//        List<OrderItemFilled> itemList = event.getItems().stream()
//                .map(item ->
//                        new OrderItemFilled(item.getProductId(), item.getQuantity(), item.getPrice()))
//                .toList();
//
//        BigDecimal totalAmount = orderWorkflow.updateTotalAmount(event.getOrderId(), itemList);
//
//        orderEventProducer.publishStartPayment(StartPaymentEvent.builder()
//                .orderId(event.getOrderId())
//                .reservationId(event.getReservationId())
//                .amount(totalAmount)
//                .build());
//    }
//
//    @KafkaListener(topics = "inventory.status", groupId = "order-service")
//    public void handleStockReserveFailed(StockFailedEvent event) {
//
//    }
//
//    @KafkaListener(topics = "payments.lifecycle", groupId = "order-service")
//    public void handlePaymentSuccess(PaymentSuccessEvent event) {
//        orderEventProducer.publishConfirmStock(ConfirmReservedEvent.builder()
//                .reservationId(event.getReservationId())
//                .build());
//    }
//
//    @KafkaListener(topics = "payments.lifecycle", groupId = "order-service")
//    public void handlePaymentFailed(PaymentFailedEvent event) {
//
//    }
//
//
//
//}