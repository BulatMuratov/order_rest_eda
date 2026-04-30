package com.bulka.infrastructure.client.inventory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryProductResponse {
    private Long productId;
    private String name;
    private String description;
    private Integer availableQuantity;
}
