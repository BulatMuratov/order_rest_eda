package com.bulka.infrastructure.client.inventory.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class InventoryReserveResponse {
    private Long reservationId;
    private List<InventoryReserveItemResponse> items;
    private Boolean success;
    private String errorMessage;
}
