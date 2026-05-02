package com.bulka.dto.event;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockReservedEvent {
    private Long orderId;
    private Long reservationId;
    private List<ReservationItemEvent> items;
}
