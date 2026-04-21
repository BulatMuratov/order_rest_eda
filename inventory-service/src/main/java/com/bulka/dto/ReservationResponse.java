package com.bulka.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReservationResponse {
    private Long reservationId;
    private List<ReservationItemResponse> items;
}
