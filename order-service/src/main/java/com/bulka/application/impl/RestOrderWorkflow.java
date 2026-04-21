package com.bulka.application.impl;

import com.bulka.infrastructure.client.inventory.ConfirmReserveResponse;
import com.bulka.infrastructure.client.inventory.InventoryClient;
//import com.bulka.infrastructure.client.inventory.InventoryCheckRequest;
//import com.bulka.infrastructure.client.inventory.InventoryCheckResponse;
import com.bulka.infrastructure.client.inventory.InventoryProductResponse;
import com.bulka.infrastructure.client.inventory.InventoryReserveItemRequest;
import com.bulka.infrastructure.client.inventory.InventoryReserveItemResponse;
import com.bulka.infrastructure.client.inventory.InventoryReserveRequest;
import com.bulka.infrastructure.client.inventory.InventoryReserveResponse;
import com.bulka.infrastructure.client.inventory.ProductStatus;
import com.bulka.infrastructure.client.payment.PaymentClient;
import com.bulka.infrastructure.client.payment.PaymentRequest;
import com.bulka.infrastructure.client.payment.PaymentResponse;
import com.bulka.infrastructure.client.payment.PaymentStatus;
import com.bulka.dto.OrderItemDto;
import com.bulka.dto.OrderItemFilled;
import com.bulka.dto.request.OrderRequest;
import com.bulka.dto.response.OrderDetailsResponse;
import com.bulka.dto.response.OrderItemDetailsResponse;
import com.bulka.dto.response.OrderResponse;
import com.bulka.dto.OrderStatus;
import com.bulka.dto.response.ProductResponse;
import com.bulka.application.OrderWorkflow;
import com.bulka.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Profile("rest")
@RequiredArgsConstructor
public class RestOrderWorkflow implements OrderWorkflow {
    private final OrderService orderService;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;

    @Override
    public OrderResponse createOrder(OrderRequest request) {


        OrderResponse order = orderService.createOrder(request.getItems());

        List<InventoryReserveItemRequest> inventoryReserveItemRequests = request.getItems().stream()
                .map(orderItem -> new InventoryReserveItemRequest(orderItem.getProductId(), orderItem.getQuantity()))
                .toList();
        try{
            InventoryReserveResponse response =  inventoryClient.reserve(InventoryReserveRequest.builder()
                    .orderId(order.getId())
                    .items(inventoryReserveItemRequests)
                    .build());

            List<OrderItemFilled> orderItemList = response.getItems().stream()
                    .map(item -> new OrderItemFilled(
                            item.getProductId(),
                            item.getQuantity(),
                            item.getPrice()))
                    .toList();

            BigDecimal totalAmount = orderItemList.stream()
                    .map(OrderItemFilled::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            orderService.updateAmount(order.getId(), orderItemList);
            order.setAmount(totalAmount);

            PaymentResponse payment = paymentClient.pay(
                    PaymentRequest.builder()
                            .orderId(order.getId())
                            .amount(totalAmount)
                            .build()
            );

            ConfirmReserveResponse confirmReserveResponse = inventoryClient.confirmReserve(response.getReservationId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


//        List<InventoryCheckRequest> inventoryCheckRequestsList = request.getItems().stream()
//                .map(i -> new InventoryCheckRequest(i.getProductId(), i.getQuantity()))
//                .toList();

//        List<InventoryCheckResponse> inventoryResponsesList = inventoryClient.checkStock(inventoryCheckRequestsList);

//        boolean allAvailable = inventoryResponsesList.stream()
//                .allMatch(el -> el.getStatus().equals(ProductStatus.AVAILABLE));
//
//        if(!allAvailable){
//            throw new RuntimeException("no stock");
//        }


//
//        List<OrderItemFilled> orderItemFilledList = inventoryResponsesList.stream()
//                .map(i -> new OrderItemFilled(i.getProductId(), i.getRequested(), i.getPrice()))
//                .toList();
//
//
//        try {
//
//
//            orderService.updateOrderStatus(
//                    order.getId(),
//                    payment.getPaymentStatus().equals(PaymentStatus.SUCCESS)
//                            ? OrderStatus.PAID : OrderStatus.FAILED
//            );
//            order.setStatus(payment.getPaymentStatus().equals(PaymentStatus.SUCCESS)
//                    ? OrderStatus.COMPLETED.toString() : OrderStatus.FAILED.toString());
//
//            inventoryClient.reserve(inventoryCheckRequestsList);
//            orderService.updateOrderStatus(
//                    order.getId(),
//                    OrderStatus.RESERVED
//            );
//            order.setStatus(OrderStatus.RESERVED.toString());
//            orderService.updateOrderStatus(
//                    order.getId(),
//                    OrderStatus.COMPLETED
//            );
//            order.setStatus(OrderStatus.COMPLETED.toString());
//        } catch (Exception e){
//            orderService.updateOrderStatus(order.getId(), OrderStatus.FAILED);
//            order.setStatus(OrderStatus.FAILED.toString());
//        }
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
