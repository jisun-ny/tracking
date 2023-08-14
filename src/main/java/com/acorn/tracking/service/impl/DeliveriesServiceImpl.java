package com.acorn.tracking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acorn.tracking.service.DeliveriesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveriesServiceImpl implements DeliveriesService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveriesServiceImpl.class);
    
    @Override
    public void autoInsertDeliveries() {

    }
}
