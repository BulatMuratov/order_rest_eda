package com.bulka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemFilled {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
