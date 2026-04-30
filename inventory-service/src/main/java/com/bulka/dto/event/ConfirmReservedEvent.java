package com.bulka.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmReservedEvent {
    private Long reservationId;
}
