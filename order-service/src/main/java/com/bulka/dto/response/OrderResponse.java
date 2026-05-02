package com.bulka.dto.response;

import com.bulka.dto.OrderStatus;
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

    public void markReserved(){
        if(!status.equals(OrderStatus.CREATED.toString())){
            throw new IllegalStateException("Invalid state");
        }
        status = OrderStatus.RESERVED.toString();
    }

    public void markPaid(){
        if(!status.equals(OrderStatus.RESERVED.toString())){
            throw new IllegalStateException("Invalid state");
        }
        status = OrderStatus.PAID.toString();
    }
    public void markCompleted(){
        if(!status.equals(OrderStatus.PAID.toString())){
            throw new IllegalStateException("Invalid state");
        }
        status = OrderStatus.COMPLETED.toString();
    }
    public void markFailed(){
        status = OrderStatus.FAILED.toString();
    }
}
