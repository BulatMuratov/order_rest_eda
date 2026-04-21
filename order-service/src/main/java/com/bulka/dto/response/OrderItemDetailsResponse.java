package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDetailsResponse {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private ProductResponse product;
}
