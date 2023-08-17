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
    public void autoInsertOrderDetails(int order_id, List<Products> randomProducts) {
        try {
            for (Products product : randomProducts) {
                OrderDetails orderDetails = OrderDetails.builder()
                        .order_id(order_id)
                        .product_id(product.getProduct_id())
                        .quantity(1)
                        .price(product.getPrice())
                        .build();
                orderDetailsMapper.autoInsertOrderDetails(orderDetails);
                deliveriesGenerator.autoInsertDeliveries(order_id, product.getProduct_id());
            }
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    private void handleDataAccessException(DataAccessException e) {
        log.error("An error occurred while inserting order details", e);
        throw new RuntimeException("An error occurred while inserting order details", e);
    }
}
