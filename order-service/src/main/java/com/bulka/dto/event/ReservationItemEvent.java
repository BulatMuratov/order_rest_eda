package com.bulka.dto.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReservationItemEvent {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
