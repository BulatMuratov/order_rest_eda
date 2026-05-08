package com.bulka.infrastructure.kafka.consumer;

import com.bulka.domain.service.InventoryService;
import com.bulka.dto.event.OrderCreatedEvent;
import com.bulka.dto.event.ReservationItemEvent;
import com.bulka.dto.event.StockFailedEvent;
import com.bulka.dto.event.StockReservedEvent;
import com.bulka.dto.request.ReservationItemRequest;
import com.bulka.dto.request.ReservationRequest;
import com.bulka.dto.response.ReservationResponse;
import com.bulka.infrastructure.kafka.producer.InventoryEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@KafkaListener(id = "inventory-lifecycle-group", topics = "orders.lifecycle", groupId = "inventory-service")
public class InventoryLifecycleConsumer {

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;

    @KafkaHandler
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        List<ReservationItemRequest> items = orderCreatedEvent.getItems().stream()
                .map(item -> new ReservationItemRequest(item.getProductId(), item.getQuantity()))
                .toList();

        try {
            ReservationResponse response = inventoryService.reserve(ReservationRequest.builder()
                    .orderId(orderCreatedEvent.getOrderId())
                    .items(items)
                    .build());
            if(response.isSuccess()){
                List<ReservationItemEvent> itemList = response.getItems().stream()
                        .map(item -> ReservationItemEvent.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build())
                        .toList();
                inventoryEventProducer.publishStockReserved(StockReservedEvent.builder()
                        .orderId(orderCreatedEvent.getOrderId())
                        .reservationId(response.getReservationId())
                        .items(itemList)
                        .build());
            }
            else{
                inventoryEventProducer.publishStockFailed(StockFailedEvent.builder()
                        .orderId(orderCreatedEvent.getOrderId())
                        .errorMessage(response.getErrorMessage())
                        .build());
            }
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
