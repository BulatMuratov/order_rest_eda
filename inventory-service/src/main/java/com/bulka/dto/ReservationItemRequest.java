package com.bulka.dto;

import lombok.Data;

@Data
public class ReservationItemRequest {
    private Long productId;
    private Integer quantity;
}
