package com.bulka.application.mapper;

import com.bulka.dto.request.OrderItemRequest;
import com.bulka.infrastructure.client.inventory.dto.request.InventoryReserveItemRequest;
import org.springframework.stereotype.Component;

@Component
public class InventoryReserveItemMapper {
    public InventoryReserveItemRequest map(OrderItemRequest  orderItemRequest) {
        return new InventoryReserveItemRequest(orderItemRequest.getProductId(), orderItemRequest.getQuantity());
    }
}
