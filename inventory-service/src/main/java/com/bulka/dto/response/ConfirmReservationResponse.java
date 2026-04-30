package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConfirmReservationResponse {
    private Long reservationId;
    private String status;
    private boolean success;
    private String errorMessage;

    public static ConfirmReservationResponse success(Long id, List<ReservationItemResponse> items) {
        return ConfirmReservationResponse.builder()
                .reservationId(id)
                .success(true)
                .build();
    }

    public static ReservationResponse failure(String message) {
        return ReservationResponse.builder()
                .success(false)
                .errorMessage(message)
                .build();
    }
}
