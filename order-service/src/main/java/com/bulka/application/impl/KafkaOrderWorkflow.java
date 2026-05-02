package com.bulka.application.impl;

import com.bulka.application.OrderWorkflow;
import com.bulka.domain.service.OrderService;
import com.bulka.dto.OrderItemFilled;
import com.bulka.dto.event.OrderCreatedEvent;
import com.bulka.dto.event.OrderItemEvent;
import com.bulka.dto.request.OrderRequest;
import com.bulka.dto.response.OrderDetailsResponse;
import com.bulka.dto.response.OrderResponse;
import com.bulka.infrastructure.kafka.producer.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
//@Profile("kafka")
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


        return null;

    }

    @Override
    public OrderResponse getOrder(Long id) {
        return null;
    }

    @Override
    public OrderDetailsResponse getOrderDetails(Long id) {
        return null;
    }

    public BigDecimal updateTotalAmount(Long orderId, List<OrderItemFilled> orderItemList) {
        BigDecimal totalAmount = orderItemList.stream()
                .map(OrderItemFilled::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderService.updateAmount(orderId, totalAmount);
        return totalAmount;
    }

    public void cancelOrder(){

    }
}
