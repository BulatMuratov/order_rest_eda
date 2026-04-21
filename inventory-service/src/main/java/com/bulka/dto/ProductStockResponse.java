package com.bulka.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductStockResponse {
    private Long productId;
    private Integer requested;
    private Integer available;
    private BigDecimal price;
    private ProductStatus status;
}
