package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReservationItemResponse {
    Long productId;
    Integer quantity;
    BigDecimal price;
}
