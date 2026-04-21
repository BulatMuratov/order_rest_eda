package com.bulka.application;

import com.bulka.dto.request.OrderRequest;
import com.bulka.dto.response.OrderDetailsResponse;
import com.bulka.dto.response.OrderResponse;

public interface OrderWorkflow {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrder(Long id);
    OrderDetailsResponse getOrderDetails(Long id);
}
