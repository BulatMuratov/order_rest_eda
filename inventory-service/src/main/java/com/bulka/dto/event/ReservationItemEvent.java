package com.bulka.dto.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReservationItemEvent {
    Long productId;
    Integer quantity;
    BigDecimal price;
}
