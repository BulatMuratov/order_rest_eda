package com.bulka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmReservationResponse {
    private Long reservationId;
    private String status;
}
