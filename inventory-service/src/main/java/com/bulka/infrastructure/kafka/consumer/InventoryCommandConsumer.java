package com.bulka.infrastructure.kafka.consumer;

import com.bulka.domain.service.InventoryService;
import com.bulka.dto.event.CancelReservationEvent;
import com.bulka.dto.event.ConfirmReservedEvent;
import com.bulka.infrastructure.kafka.producer.InventoryEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(id = "inventory-command-id", topics = "inventory.commands", groupId = "inventory-service")
public class InventoryCommandConsumer {

    private final InventoryService inventoryService;
//    private final InventoryEventProducer inventoryEventProducer;

    @KafkaHandler
    public void handleConfirmReserved(ConfirmReservedEvent confirmReservedEvent) {
        try{
            inventoryService.confirmReservation(confirmReservedEvent.getReservationId());
        }
        catch (Exception ex){
            throw ex;
        }
    }

    @KafkaHandler
    public void handleCancelReserved(CancelReservationEvent event) {
        try{
            inventoryService.releaseReserve(event.getReservationId());
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
