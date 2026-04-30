package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long productId;
    private String name;
    private String description;
    private Integer availableQuantity;
}

