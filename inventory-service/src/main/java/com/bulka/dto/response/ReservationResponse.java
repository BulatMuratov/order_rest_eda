package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReservationResponse {
    private Long reservationId;
    private List<ReservationItemResponse> items;
    private boolean success;
    private String errorMessage;

    public static ReservationResponse success(Long id, List<ReservationItemResponse> items) {
        return ReservationResponse.builder()
                .reservationId(id)
                .items(items)
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
