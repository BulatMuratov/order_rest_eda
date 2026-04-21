package com.bulka.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReservationRequest {
    private Long orderId;
    private List<ReservationItemRequest> items;
}
