package com.bulka.application.mapper;

import com.bulka.dto.OrderItemFilled;
import com.bulka.infrastructure.client.inventory.dto.response.InventoryReserveItemResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderItemFilledMapper {
    public OrderItemFilled map(InventoryReserveItemResponse itemResponse) {
        return new OrderItemFilled(
                itemResponse.getProductId(),
                itemResponse.getQuantity(),
                itemResponse.getPrice());
    }
}
