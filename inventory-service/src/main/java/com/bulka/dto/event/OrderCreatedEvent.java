package com.bulka.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private List<OrderItemEvent> items;
}
