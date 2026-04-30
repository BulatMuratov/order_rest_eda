package com.bulka.infrastructure.client.inventory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmReserveResponse {
    private Long reservationId;
    private String status;
}
