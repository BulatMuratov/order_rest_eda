package com.bulka.infrastructure.client.inventory.dto.response;

import lombok.Data;

@Data
public class InventoryProductResponse {
    private Long productId;
    private String name;
    private String description;
    private Integer availableQuantity;
}
