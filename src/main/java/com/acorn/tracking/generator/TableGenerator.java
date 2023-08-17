package com.acorn.tracking.generator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.mapper.TableMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableGenerator {

    private final TableMapper tableMapper;

    @Transactional
    public void resetDatabase() {
        try {
            tableMapper.dropOrderDetails();
            tableMapper.dropRecalls();
            tableMapper.dropBaskets();
            tableMapper.dropDeliveries();
            tableMapper.dropOrders();
            tableMapper.dropProducts();
            tableMapper.dropAdmins();

            tableMapper.createAdmins();
            tableMapper.createProducts();
            tableMapper.createOrders();
            tableMapper.createDeliveries();
            tableMapper.createBaskets();
            tableMapper.createRecalls();
            tableMapper.createOrderDetails();
        } catch (Exception e) {
            log.error("Database reset failed", e);
            throw new RuntimeException("Database reset failed", e);
        }
    }
}
