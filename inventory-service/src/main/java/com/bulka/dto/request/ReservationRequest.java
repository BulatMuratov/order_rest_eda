package com.bulka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ReservationRequest {
    private Long orderId;
    private List<ReservationItemRequest> items;
}
