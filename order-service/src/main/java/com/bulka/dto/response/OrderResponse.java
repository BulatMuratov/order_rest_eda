package com.bulka.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
