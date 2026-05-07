package com.bulka.application.impl;

import com.bulka.application.OrderWorkflow;
import com.bulka.domain.service.OrderService;
import com.bulka.dto.OrderItemFilled;
import com.bulka.dto.OrderStatus;
import com.bulka.dto.event.ConfirmReservedEvent;
import com.bulka.dto.event.OrderCreatedEvent;
import com.bulka.dto.event.OrderItemEvent;
import com.bulka.dto.event.StartPaymentEvent;
import com.bulka.dto.request.OrderRequest;
import com.bulka.dto.response.OrderDetailsResponse;
import com.bulka.dto.response.OrderResponse;
import com.bulka.infrastructure.kafka.producer.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
//@Profile("kafka")
@Primary
@RequiredArgsConstructor
public class KafkaOrderWorkflow implements OrderWorkflow {

    private final OrderService orderService;
    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        OrderResponse order = orderService.createOrder(request.getItems());

        List<OrderItemEvent>  orderItemEvents = request.getItems().stream()
                .map(orderItem -> new OrderItemEvent(orderItem.getProductId(), orderItem.getQuantity()))
                .toList();

        orderEventProducer.publishOrderCreated(OrderCreatedEvent.builder()
                .orderId(order.getId())
                .items(orderItemEvents)
                .build());


        return order;

    }

    @Override
    public OrderResponse getOrder(Long id) {
        return null;
    }

    @Override
    public OrderDetailsResponse getOrderDetails(Long id) {
        return null;
    }

    @Transactional
    public void confirmReservation(Long orderId, Long reservationId, List<OrderItemFilled> orderItemList) {
        orderService.updateAmountOrderItemList(orderId, orderItemList);

        BigDecimal totalAmount = orderItemList.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderService.updateAmount(orderId, totalAmount);
        orderService.updateOrderStatus(orderId, OrderStatus.RESERVED);

        orderEventProducer.publishStartPayment(StartPaymentEvent.builder()
                .orderId(orderId)
                .reservationId(reservationId)
                .amount(totalAmount)
                .build());
    }

    @Transactional
    public void finishOrder(Long orderId, Long reservationId) {
        orderService.updateOrderStatus(orderId, OrderStatus.PAID);

        orderEventProducer.publishConfirmStock(ConfirmReservedEvent.builder()
                .reservationId(reservationId)
                .build());

        //временное решение, на случай добавления события о подтверждении - убрать
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    public void cancelOrder(){

    }
}
