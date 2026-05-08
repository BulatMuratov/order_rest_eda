package com.bulka.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelReservationEvent {
    private Long orderId;
    private Long reservationId;
    private String message;
}
