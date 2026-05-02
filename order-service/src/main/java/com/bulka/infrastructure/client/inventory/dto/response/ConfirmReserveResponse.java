package com.bulka.infrastructure.client.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmReserveResponse {
    private Long reservationId;
    private Boolean success;
    private String errorMessage;
}
