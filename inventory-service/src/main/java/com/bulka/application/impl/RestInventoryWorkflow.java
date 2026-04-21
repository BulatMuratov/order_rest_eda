package com.bulka.application.impl;

import com.bulka.application.InventoryWorkflow;
import com.bulka.domain.service.InventoryService;
import com.bulka.dto.ConfirmReservationResponse;
import com.bulka.dto.ProductResponse;
import com.bulka.dto.ReservationRequest;
import com.bulka.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("rest")
@RequiredArgsConstructor
public class RestInventoryWorkflow implements InventoryWorkflow {

    private final InventoryService inventoryService;

    public List<ProductResponse> getProducts(List<Long> productIds) {
        return inventoryService.getProducts(productIds);
    }

    public ReservationResponse reserve(ReservationRequest reservationRequest) {
        return inventoryService.reserve(reservationRequest);
    }

    public ConfirmReservationResponse confirmReservation(Long reservationId){
        return inventoryService.confirmReservation(reservationId);
    }


}
