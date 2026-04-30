package com.bulka.infrastructure.client.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InventoryReserveItemRequest {
    private Long productId;
    private Integer quantity;
}
