package com.acorn.tracking.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.mapper.TableMapper;
import com.acorn.tracking.service.TableService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableMapper tableMapper;

    @Override
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
            throw new RuntimeException("Database reset failed", e);
        }
    }
}
