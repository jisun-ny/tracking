package com.acorn.tracking.service.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.acorn.tracking.domain.Orders;
import com.acorn.tracking.mapper.OrdersMapper;
import com.acorn.tracking.service.OrdersService;
import com.github.javafaker.Faker;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Override
    @Scheduled(initialDelay = 5000, fixedRate = 10000)
    public void autoInsertOrders() {
        try {
            Faker faker = new Faker();
            InputStream inputStream = getOrdersJsonInputStream();
            List<Orders> jsonOrderInfo = readOrdersFromJson(inputStream);
            Orders orderInfo = jsonOrderInfo.get(faker.random().nextInt(jsonOrderInfo.size()));
            logger.info("고객 이름: " + orderInfo.getCustomer_name());
            logger.info("위도: " + orderInfo.getLatitude());
            logger.info("경도: " + orderInfo.getLongitude());
        } catch (FileNotFoundException e) {
            handleFileNotFoundException(e);
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
    }
}
