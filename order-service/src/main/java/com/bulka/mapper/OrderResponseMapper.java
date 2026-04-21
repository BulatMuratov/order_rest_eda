package com.bulka.mapper;

import com.bulka.domain.model.Order;
import com.bulka.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderResponseMapper implements Mapper<Order, OrderResponse> {
    @Override
    public OrderResponse toDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().toString())
                .amount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
