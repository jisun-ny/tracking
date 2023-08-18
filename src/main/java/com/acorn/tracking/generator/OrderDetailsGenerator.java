package com.acorn.tracking.generator;

import org.springframework.dao.DataAccessException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.OrderDetails;
import com.acorn.tracking.domain.Products;
import com.acorn.tracking.mapper.OrderDetailsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDetailsGenerator {

    private final OrderDetailsMapper orderDetailsMapper;
    private final DeliveriesGenerator deliveriesGenerator;

    @Transactional
    public void autoInsertOrderDetails(int orderId, List<Products> randomProducts) {
        try {
            randomProducts.forEach(product -> insertOrderDetailAndDelivery(orderId, product));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    private void insertOrderDetailAndDelivery(int orderId, Products product) {
        orderDetailsMapper.autoInsertOrderDetails(createOrderDetails(orderId, product));
        deliveriesGenerator.autoInsertDeliveries(orderId, product.getProduct_id());
    }

    private OrderDetails createOrderDetails(int orderId, Products product) {
        return OrderDetails.builder()
                .order_id(orderId)
                .product_id(product.getProduct_id())
                .quantity(1)
                .price(product.getPrice())
                .build();
    }

    private void handleDataAccessException(DataAccessException e) {
        log.error("An error occurred while inserting order details", e);
        throw new RuntimeException("An error occurred while inserting order details", e);
    }
}
