package com.bulka.infrastructure.client.inventory;

import lombok.Data;

import java.util.List;

@Data
public class InventoryReserveResponse {
    private Long reservationId;
    private List<InventoryReserveItemResponse> items;
}
