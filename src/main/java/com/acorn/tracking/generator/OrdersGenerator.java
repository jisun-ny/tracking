package com.acorn.tracking.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.Orders;
import com.acorn.tracking.domain.Products;
import com.acorn.tracking.mapper.OrdersMapper;
import com.acorn.tracking.mapper.ProductsMapper;
import com.github.javafaker.Faker;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            Faker faker = new Faker();
            InputStream inputStream = getOrdersJsonInputStream();
            List<Orders> jsonOrderInfo = readOrdersFromJson(inputStream);
            Orders orderInfo = jsonOrderInfo.get(faker.random().nextInt(jsonOrderInfo.size()));
            List<Products> randomProducts = productsMapper.getRandomProducts(faker.random().nextInt(1, 5));
            int totalOrdered = randomProducts.size();
            int totalPrice = randomProducts.stream().mapToInt(Products::getPrice).sum();
            Orders order = createOrder(totalOrdered, totalPrice, orderInfo);
            insertOrders(order, randomProducts);
            orderDetailsGenerator.autoInsertOrderDetails(ordersMapper.getLastInsertOrderId(), randomProducts);
        } catch (FileNotFoundException e) {
            handleFileNotFoundException(e);
        } catch (IOException e) {
            handleIOException(e);
        }
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

    private InputStream getOrdersJsonInputStream() throws FileNotFoundException {
        InputStream inputStream = getClass().getResourceAsStream("/static/Orders.json");
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: Orders.json");
        }
        return inputStream;
    }

    private List<Orders> readOrdersFromJson(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return new GsonBuilder().create().fromJson(reader, new TypeToken<List<Orders>>() {
            }.getType());
        }
    }

    private void handleFileNotFoundException(FileNotFoundException e) {
        log.error("File not found: Orders.json", e);
        throw new RuntimeException("File not found: Orders.json", e);
    }

    private void handleIOException(IOException e) {
        log.error("An error occurred while reading the orders from the JSON file", e);
        throw new RuntimeException("An error occurred while reading the orders from the JSON file", e);
    }
}
