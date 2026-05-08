package com.bulka.domain.service;

import com.bulka.dto.response.ConfirmReservationResponse;
import com.bulka.dto.response.ProductResponse;
import com.bulka.dto.request.ReservationItemRequest;
import com.bulka.dto.response.ReservationItemResponse;
import com.bulka.dto.request.ReservationRequest;
import com.bulka.dto.response.ReservationResponse;
import com.bulka.domain.model.Product;
import com.bulka.domain.model.Reservation;
import com.bulka.domain.model.ReservationItem;
import com.bulka.domain.model.ReservationStatus;
import com.bulka.infrastructure.repository.ProductRepository;
import com.bulka.infrastructure.repository.ReservationItemRepository;
import com.bulka.infrastructure.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationItemRepository reservationItemRepository;

//    @Transactional(readOnly = true)
//    public List<OrderItemStockResponse> checkInventory(List<OrderItemRequest> orderItemList) {
//        List<Long> productIds = orderItemList.stream()
//                .map(OrderItemRequest::getProductId)
//                .toList();
//
//        List<Product> productsList = (List<Product>) productRepository.findAllById(productIds);
//        Map<Long, Product> productMap = productsList.stream().collect(Collectors.toMap(Product::getId, product -> product));
//        List<OrderItemStockResponse> orderItemStockResponseList = new ArrayList<>();
//
//        for (OrderItemRequest orderItemRequest : orderItemList) {
//            Product product = productMap.get(orderItemRequest.getProductId());
//
//            OrderItemStockResponse orderItemStockResponse = OrderItemStockResponse.builder()
//                    .productId(orderItemRequest.getProductId())
//                    .requested(orderItemRequest.getQuantity())
//                    .build();
//            if(product == null){
//                orderItemStockResponse.setPrice(BigDecimal.ZERO);
//                orderItemStockResponse.setAvailable(0);
//                orderItemStockResponse.setStatus(ProductStatus.NOT_FOUND);
//            }
//            else {
//                orderItemStockResponse.setPrice(product.getPrice());
//                orderItemStockResponse.setAvailable(product.getStock());
//                orderItemStockResponse.setStatus(orderItemRequest.getQuantity() <= product.getStock()
//                        ? ProductStatus.AVAILABLE : ProductStatus.INSUFFICIENT);
//            }
//            orderItemStockResponseList.add(orderItemStockResponse);
//        }
//
//        return orderItemStockResponseList;
//
//    }

    public List<ProductResponse> getProducts(List<Long> productIds){
        List<Product> products = (List<Product>) productRepository.findAllById(productIds);
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : products) {
            productResponseList.add(ProductResponse.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .availableQuantity(product.getStock())
                    .build());
        }
        return productResponseList;

    }

    @Transactional
    public ReservationResponse reserve(ReservationRequest reservationRequest) {
        Reservation existing = reservationRepository.findByOrderId(reservationRequest.getOrderId());
        if(existing != null){
            return getReservation(existing);
        }

        List<ReservationItemResponse> reservationItemListResponse = new ArrayList<>();
        List<ReservationItem> reservationItemList = new ArrayList<>();

        Reservation reservation = Reservation.builder()
                .orderId(reservationRequest.getOrderId())
                .status(ReservationStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            for (ReservationItemRequest item : reservationRequest.getItems()) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                if (product == null) {
                    return ReservationResponse.failure("Product %d not found".formatted(item.getProductId()));
                }

                int updated = productRepository.reserve(
                        item.getProductId(),
                        item.getQuantity()
                );

                if (updated == 0) {
                    return ReservationResponse.failure("ProductId - %d not enough stock".formatted(item.getProductId()));
                }

                reservationItemList.add(ReservationItem.builder()
                        .reservation(reservation)
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(product.getPrice())
                        .build());

                reservationItemListResponse.add(ReservationItemResponse.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(product.getPrice())
                        .build()
                );
            }

            reservation.addItems(reservationItemList);
            reservation.setStatus(ReservationStatus.RESERVED);
            Reservation saveReservation = reservationRepository.save(reservation);

            return ReservationResponse.success(saveReservation.getId(), reservationItemListResponse);
        }
        catch (Exception e){
            return ReservationResponse.failure("Internal error occurred. %s".formatted(e.getMessage()));
        }
    }

    @Transactional
    public void releaseReserve(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation id %d not found".formatted(reservationId)));

        if(reservation.getStatus() == ReservationStatus.CONFIRMED ||
            reservation.getStatus() == ReservationStatus.EXPIRED ||
            reservation.getStatus() == ReservationStatus.FAILED) {
            return;
        }

        List<ReservationItem> itemList = reservationItemRepository.findAllByReservationId(reservationId);

        for(ReservationItem item: itemList){
            productRepository.increaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        reservation.setStatus(ReservationStatus.FAILED);
    }

    @Transactional
    public ConfirmReservationResponse confirmReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        if(reservation.getStatus() != ReservationStatus.RESERVED){
            return ConfirmReservationResponse.builder()
                    .success(false)
                    .errorMessage("Reservation status is not RESERVE")
                    .build();
        }

//        List<ReservationItem> reservationItemList = reservationItemRepository.findAllByReservationId(reservationId);

//        for(ReservationItem item: reservationItemList){
//            int updated = productRepository.deductStock(
//                    item.getProductId(),
//                    item.getQuantity()
//            );
//
//            if (updated == 0) {
//                throw new RuntimeException("Not enough stock or concurrent update");
//            }
//        }

        reservation.setStatus(ReservationStatus.CONFIRMED);

        return ConfirmReservationResponse.builder()
                .reservationId(reservationId)
                .status(ReservationStatus.CONFIRMED.toString())
                .success(true)
                .build();
    }

    @Transactional
    public void cancelReservation(Long orderId, Long reservationId){

    }

    private ReservationResponse getReservation(Reservation reservation){
        List<ReservationItemResponse> result = new ArrayList<>();
        List<ReservationItem> reservationItemList = reservationItemRepository.findAllByReservationId(reservation.getId());
        for(ReservationItem item: reservationItemList){
            result.add(ReservationItemResponse.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build());
        }

        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .items(result)
                .success(true)
                .build();
    }
}
