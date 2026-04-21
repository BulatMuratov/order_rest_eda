package com.bulka.application;

import com.bulka.dto.ConfirmReservationResponse;
import com.bulka.dto.ProductResponse;
import com.bulka.dto.ReservationRequest;
import com.bulka.dto.ReservationResponse;

import java.util.List;

public interface InventoryWorkflow {
    List<ProductResponse> getProducts(List<Long> productIds);

    ReservationResponse reserve(ReservationRequest reservationRequest);

    ConfirmReservationResponse confirmReservation(Long reservationId);
}
