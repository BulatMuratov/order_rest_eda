package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailsResponse {
    private Long id;
    private String status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private List<OrderItemDetailsResponse> itemList;
}
