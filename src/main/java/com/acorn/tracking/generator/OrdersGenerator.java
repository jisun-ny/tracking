package com.acorn.tracking.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.acorn.tracking.domain.Orders;
import com.acorn.tracking.domain.Products;
import com.acorn.tracking.mapper.OrdersMapper;
import com.acorn.tracking.mapper.ProductsMapper;
import com.github.javafaker.Faker;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrdersGenerator {

    private final ProductsMapper productsMapper;
    private final OrdersMapper ordersMapper;
    private final OrderDetailsGenerator orderDetailsGenerator;

    @Transactional
    @Scheduled(initialDelay = 3000, fixedRate = 30000)
    public void autoInsertOrders() {
        try {
            processOrders();
        } catch (Exception e) {
            handleException(e, "An error occurred while processing orders");
        }
    }

    private void processOrders() throws IOException {
        Faker faker = new Faker();
        List<Orders> jsonOrderInfo = readOrdersFromJson(getClass().getResourceAsStream("/static/Orders.json"));
        Orders orderInfo = jsonOrderInfo.get(faker.random().nextInt(jsonOrderInfo.size()));
        List<Products> randomProducts = productsMapper.getRandomProducts(faker.random().nextInt(1, 5));
        Orders order = createOrder(randomProducts.size(), randomProducts.stream().mapToInt(Products::getPrice).sum(), orderInfo);
        insertOrders(order, randomProducts);
        orderDetailsGenerator.autoInsertOrderDetails(ordersMapper.getLastInsertOrderId(), randomProducts);
    }

    private Orders createOrder(int totalOrdered, int totalPrice, Orders orderInfo) {
        return Orders.builder()
                .customer_name(orderInfo.getCustomer_name())
                .quantity_ordered(totalOrdered)
                .total_price(totalPrice)
                .latitude(orderInfo.getLatitude())
                .longitude(orderInfo.getLongitude())
                .build();
    }

    private void insertOrders(Orders orders, List<Products> randomProducts) {
        try {
            ordersMapper.autoInsertOrders(orders);
            for (Products product : randomProducts) {
                productsMapper.inventoryReduction(product.getProduct_id(), 1);
            }
        } catch (Exception e) {
            log.error("An error occurred while inserting orders", e);
            throw new RuntimeException("An error occurred while inserting orders", e);
        }
    }

    private List<Orders> readOrdersFromJson(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return new GsonBuilder().create().fromJson(reader, new TypeToken<List<Orders>>() {
            }.getType());
        }
    }

    private void handleException(Exception e, String errorMessage) {
        log.error(errorMessage, e);
        throw new RuntimeException(errorMessage, e);
    }
}
