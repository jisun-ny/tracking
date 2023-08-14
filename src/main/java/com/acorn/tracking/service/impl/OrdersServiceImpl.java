package com.acorn.tracking.service.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.Orders;
import com.acorn.tracking.domain.Products;
import com.acorn.tracking.mapper.OrdersMapper;
import com.acorn.tracking.mapper.ProductsMapper;
import com.acorn.tracking.service.OrdersService;
import com.github.javafaker.Faker;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    private final ProductsMapper productsMapper;
    private final OrdersMapper ordersMapper;

    @Override
    @Transactional
    @Scheduled(initialDelay = 5000, fixedRate = 10000)
    public void autoInsertOrders() {
        try {
            Faker faker = new Faker();
            InputStream inputStream = getOrdersJsonInputStream();
            List<Orders> jsonOrderInfo = readOrdersFromJson(inputStream);
            Orders orderInfo = jsonOrderInfo.get(faker.random().nextInt(jsonOrderInfo.size()));
            List<Products> randomProducts = productsMapper.getRandomProducts(faker.random().nextInt(1, 5));
            int totalOrdered = randomProducts.size();
            int totalPrice = randomProducts.stream().mapToInt(Products::getPrice).sum();
            Orders newOrder = createOrder(totalOrdered, totalPrice, orderInfo);
            insertOrders(newOrder);
        } catch (FileNotFoundException e) {
            handleFileNotFoundException(e);
        } catch (Exception e) {
            handleInsertionException(e);
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

    private void insertOrders(Orders orders) {
        try {
            ordersMapper.autoInsertOrders(orders);
        } catch (Exception e) {
            handleInsertionException(e);
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

    private List<Orders> readOrdersFromJson(InputStream inputStream) {
        return new GsonBuilder()
                .create().fromJson(
                        new InputStreamReader(inputStream),
                        new TypeToken<List<Orders>>() {
                        }.getType());
    }

    private void handleFileNotFoundException(FileNotFoundException e) {
        logger.error("File not found: Orders.json", e);
        throw new RuntimeException("File not found: Orders.json", e);
    }

    private void handleInsertionException(Exception e) {
        logger.error("An error occurred while inserting orders", e);
    }
}
