package com.bulka.infrastructure.client.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping("/api/v1/inventory/products")
    List<InventoryProductResponse> getProducts(List<Long> productIds);

    @PostMapping("/api/v1/inventory/reserve")
    InventoryReserveResponse reserve(InventoryReserveRequest request);

    @PostMapping("/api/v1/inventory/reserve/{reservationId}/confirm")
    ConfirmReserveResponse confirmReserve(@PathVariable Long reservationId);

}