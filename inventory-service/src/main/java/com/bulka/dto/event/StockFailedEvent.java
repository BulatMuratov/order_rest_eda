package com.bulka.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockFailedEvent {
    private Long orderId;
    private String message;
}
