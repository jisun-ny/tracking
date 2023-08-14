package com.acorn.tracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.OrderDetails;
import com.acorn.tracking.domain.Products;
import com.acorn.tracking.mapper.OrderDetailsMapper;
import com.acorn.tracking.service.OrderDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    private final OrderDetailsMapper orderDetailsMapper;

    @Override
    @Transactional
    public void autoInsertOrderDetails(int orderId, List<Products> randomProducts) {
        for (Products product : randomProducts) {
            try {
                OrderDetails orderDetails = OrderDetails.builder()
                        .order_id(orderId)
                        .product_id(product.getProduct_id())
                        .quantity(1)
                        .price(product.getPrice())
                        .build();
                orderDetailsMapper.autoInsertOrderDetails(orderDetails);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void handleException(Exception e) {
        logger.error("An error occurred while inserting order details", e);
        throw new RuntimeException("An error occurred while inserting order details", e);
    }
}
