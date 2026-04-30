package com.bulka.controller;

import com.bulka.dto.response.ConfirmReservationResponse;
import com.bulka.dto.response.ProductResponse;
import com.bulka.dto.request.ReservationRequest;
import com.bulka.dto.response.ReservationResponse;
import com.bulka.domain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


//    @PostMapping("/check")
//    public ResponseEntity<List<OrderItemStockResponse>> checkStock(@RequestBody List<OrderItemRequest> list){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(inventoryService.checkInventory(list));
//    }

    @PostMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestBody List<Long> productIds){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.getProducts(productIds));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserve(@RequestBody ReservationRequest reservationRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.reserve(reservationRequest));
    }

    @PostMapping("/reserve/{reservationId}/confirm")
    public ResponseEntity<ConfirmReservationResponse> confirmStock(@PathVariable Long reservationId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.confirmReservation(reservationId));
    }

    @PostMapping("/reserve/{reservationId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void releaseReserve(@PathVariable Long reservationId){
        inventoryService.releaseReserve(reservationId);
    }
}
