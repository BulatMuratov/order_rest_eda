package com.bulka.domain.service;

import com.bulka.dto.OrderItemDto;
import com.bulka.dto.OrderItemFilled;
import com.bulka.dto.request.OrderItemRequest;
import com.bulka.dto.response.OrderResponse;
import com.bulka.dto.OrderStatus;
import com.bulka.domain.model.Order;
import com.bulka.domain.model.OrderItem;
import com.bulka.infrastructure.repository.OrderItemRepository;
import com.bulka.infrastructure.repository.OrderRepository;
import com.bulka.mapper.OrderResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderResponseMapper orderResponseMapper;

    @Transactional
    public OrderResponse createOrder(List<OrderItemRequest> orderItems) {

        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> orderItemList = new ArrayList<>();
        for(OrderItemRequest item: orderItems) {
            orderItemList.add(OrderItem.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .build());
        }

//        BigDecimal totalAmount = orderItemList.stream()
//                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.addOrderItems(orderItemList);
//        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        return orderResponseMapper.toDto(order);
    }

    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderResponseMapper.toDto(order);
    }

    public List<OrderItemDto> getOrderItems(Long orderId){
        List<OrderItem> orderItemList = orderItemRepository.findAllByOrderId(orderId);
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for(OrderItem orderItem: orderItemList) {
            orderItemDtoList.add(OrderItemDto.builder()
                    .id(orderItem.getId())
                    .orderId(orderId)
                    .productId(orderItem.getProductId())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getPrice())
                    .build());
        }
        return orderItemDtoList;
    }

    @Transactional
    public void updateAmount(Long orderId, BigDecimal totalAmount) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setTotalAmount(totalAmount);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(orderStatus);
    }
}
