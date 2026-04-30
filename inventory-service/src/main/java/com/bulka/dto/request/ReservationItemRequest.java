package com.bulka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReservationItemRequest {
    private Long productId;
    private Integer quantity;
}
