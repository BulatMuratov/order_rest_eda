package com.bulka.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockReservedEvent {
    private Long orderId;
    private Long reservationId;
    private List<ReservationItemEvent> items;
}
