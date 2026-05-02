package com.bulka.application.impl;

import com.bulka.application.mapper.InventoryReserveItemMapper;
import com.bulka.application.mapper.OrderItemFilledMapper;
import com.bulka.dto.OrderStatus;
import com.bulka.exception.OrderConfirmException;
import com.bulka.exception.OrderPayException;
import com.bulka.exception.OrderReserveException;
import com.bulka.infrastructure.client.inventory.dto.response.ConfirmReserveResponse;
import com.bulka.infrastructure.client.inventory.InventoryClient;
import com.bulka.infrastructure.client.inventory.dto.response.InventoryProductResponse;
import com.bulka.infrastructure.client.inventory.dto.request.InventoryReserveItemRequest;
import com.bulka.infrastructure.client.inventory.dto.request.InventoryReserveRequest;
import com.bulka.infrastructure.client.inventory.dto.response.InventoryReserveResponse;
import com.bulka.infrastructure.client.payment.PaymentClient;
import com.bulka.infrastructure.client.payment.dto.request.PaymentRequest;
import com.bulka.infrastructure.client.payment.dto.response.PaymentResponse;
import com.bulka.dto.OrderItemDto;
import com.bulka.dto.OrderItemFilled;
import com.bulka.dto.request.OrderRequest;
import com.bulka.dto.response.OrderDetailsResponse;
import com.bulka.dto.response.OrderItemDetailsResponse;
import com.bulka.dto.response.OrderResponse;
import com.bulka.dto.response.ProductResponse;
import com.bulka.application.OrderWorkflow;
import com.bulka.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Primary
@Profile("rest")
@RequiredArgsConstructor
public class RestOrderWorkflow implements OrderWorkflow {
    private final OrderService orderService;

    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;

    private final InventoryReserveItemMapper inventoryReserveItemMapper;
    private final OrderItemFilledMapper orderItemFilledMapper;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Long reservationId = null;

        OrderResponse order = orderService.createOrder(request.getItems());

        List<InventoryReserveItemRequest> inventoryReserveItemRequests = request.getItems().stream()
                .map(inventoryReserveItemMapper::map)
                .toList();

        InventoryReserveResponse inventoryResponse =  inventoryClient.reserve(InventoryReserveRequest.builder()
                .orderId(order.getId())
                .items(inventoryReserveItemRequests)
                .build());

        reservationId = inventoryResponse.getReservationId();

        if(!inventoryResponse.getSuccess()){
            order.markFailed();
            orderService.updateOrderStatus(order.getId(), OrderStatus.FAILED);
            throw new OrderReserveException(inventoryResponse.getErrorMessage());
        }

        order.markReserved();
        orderService.updateOrderStatus(order.getId(), OrderStatus.RESERVED);


        List<OrderItemFilled> orderItemList = inventoryResponse.getItems().stream()
                .map(orderItemFilledMapper::map)
                .toList();

        BigDecimal totalAmount = orderItemList.stream()
                .map(OrderItemFilled::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderService.updateAmount(order.getId(), totalAmount);
        order.setAmount(totalAmount);

        PaymentResponse payment = paymentClient.pay(
                PaymentRequest.builder()
                        .orderId(order.getId())
                        .amount(totalAmount)
                        .build()
        );

        if(!payment.getSuccess()){
            inventoryClient.cancelReserve(reservationId); // компенсация

            order.markFailed();
            orderService.updateOrderStatus(order.getId(), OrderStatus.FAILED);
            throw new OrderPayException(payment.getErrorMessage());
        }


        order.markPaid();
        orderService.updateOrderStatus(order.getId(), OrderStatus.PAID);

        ConfirmReserveResponse confirmReserveResponse = inventoryClient.confirmReserve(reservationId);

        if(!confirmReserveResponse.getSuccess()){
            paymentClient.refund(order.getId()); // компенсация
            inventoryClient.cancelReserve(reservationId);

            order.markFailed();
            orderService.updateOrderStatus(order.getId(), OrderStatus.FAILED);
            throw new OrderConfirmException(confirmReserveResponse.getErrorMessage());
        }

        order.markCompleted();
        orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);

        return order;
    }

    public OrderResponse getOrder(Long id){
        return orderService.getOrder(id);

    }

    public OrderDetailsResponse getOrderDetails(Long id){
        OrderResponse orderResponse = getOrder(id);
        List<OrderItemDto> orderItemDtoList = orderService.getOrderItems(id);
        List<Long> productIds = orderItemDtoList.stream()
                        .map(OrderItemDto::getProductId)
                        .toList();
        Map<Long, OrderItemDto> mps = orderItemDtoList.stream()
                .collect(Collectors.toMap(
                        OrderItemDto::getProductId,  // ключ - productId
                        Function.identity(),          // значение - сам OrderItemDto
                        (existing, replacement) -> existing  // разрешение конфликтов (если есть дубликаты productId)
                ));

        List<InventoryProductResponse> responseList =  inventoryClient.getProducts(productIds);

        List<OrderItemDetailsResponse> resultList = new ArrayList<>();
        for(InventoryProductResponse response : responseList){
            resultList.add(OrderItemDetailsResponse.builder()
                            .productId(response.getProductId())
                            .quantity(mps.get(response.getProductId()).getQuantity())
                            .price(mps.get(response.getProductId()).getPrice())
                            .product(ProductResponse.builder()
                                    .name(response.getName())
                                    .description(response.getDescription())
                                    .availableQuantity(response.getAvailableQuantity())
                                    .build())
                    .build());
        }

        return OrderDetailsResponse.builder()
                .id(orderResponse.getId())
                .status(orderResponse.getStatus())
                .amount(orderResponse.getAmount())
                .createdAt(orderResponse.getCreatedAt())
                .itemList(resultList)
                .build();
    }
}
