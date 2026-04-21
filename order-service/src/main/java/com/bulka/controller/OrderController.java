package com.bulka.controller;

import com.bulka.dto.request.OrderRequest;
import com.bulka.dto.response.OrderDetailsResponse;
import com.bulka.dto.response.OrderResponse;
import com.bulka.application.OrderWorkflow;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
public class OrderController {
    private final OrderWorkflow orderWorkflow;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderWorkflow.createOrder(orderRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderWorkflow.getOrder(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderWorkflow.getOrderDetails(id));
    }
}
