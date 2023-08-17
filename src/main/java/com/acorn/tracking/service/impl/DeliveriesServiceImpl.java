package com.acorn.tracking.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acorn.tracking.domain.Deliveries;
import com.acorn.tracking.mapper.DeliveriesMapper;
import com.acorn.tracking.service.DeliveriesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveriesServiceImpl implements DeliveriesService {

    private final DeliveriesMapper deliveriesMapper;
    
    @Override
    public List<Deliveries> getLocations() {
        return deliveriesMapper.getLocations();
    }
}
