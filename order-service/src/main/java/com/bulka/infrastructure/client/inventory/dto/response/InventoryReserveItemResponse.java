package com.bulka.infrastructure.client.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InventoryReserveItemResponse {
    Long productId;
    Integer quantity;
    BigDecimal price;
}
