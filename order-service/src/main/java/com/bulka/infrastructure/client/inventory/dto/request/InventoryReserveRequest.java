package com.bulka.infrastructure.client.inventory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class InventoryReserveRequest {
    private Long orderId;
    private List<InventoryReserveItemRequest> items;
}
