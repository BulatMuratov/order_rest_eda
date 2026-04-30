package com.bulka.infrastructure.kafka.consumer;

import com.bulka.domain.service.InventoryService;
import com.bulka.dto.request.ReservationItemRequest;
import com.bulka.dto.request.ReservationRequest;
import com.bulka.dto.response.ReservationResponse;
import com.bulka.dto.event.ConfirmReservedEvent;
import com.bulka.dto.event.OrderCreatedEvent;
import com.bulka.dto.event.ReservationItemEvent;
import com.bulka.dto.event.StockFailedEvent;
import com.bulka.dto.event.StockReservedEvent;
import com.bulka.exception.NotEnoughStockException;
import com.bulka.exception.ProductNotFoundException;
import com.bulka.infrastructure.kafka.producer.InventoryEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;

    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        List<ReservationItemRequest> items = orderCreatedEvent.getItems().stream()
                .map(item -> new ReservationItemRequest(item.getProductId(), item.getQuantity()))
                .toList();

        try {
            ReservationResponse response = inventoryService.reserve(ReservationRequest.builder()
                    .orderId(orderCreatedEvent.getOrderId())
                    .items(items)
                    .build());
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
        catch (NotEnoughStockException | ProductNotFoundException ex){
            inventoryEventProducer.publishStockFailed(StockFailedEvent.builder()
                    .orderId(orderCreatedEvent.getOrderId())
                    .message(ex.getMessage())
                    .build());
        }
        catch (Exception ex){
            throw ex;
        }
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void handleConfirmReserved(ConfirmReservedEvent confirmReservedEvent) {
        try{
            inventoryService.confirmReservation(confirmReservedEvent.getReservationId());
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
