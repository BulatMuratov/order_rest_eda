package com.bulka.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    List<OrderItemRequest> items;
}

