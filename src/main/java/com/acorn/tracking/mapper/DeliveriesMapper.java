package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.Deliveries;

@Mapper
public interface DeliveriesMapper {
    void autoInsertDeliveries(Deliveries deliveries);
    int getLastInsertDeliveriesId();
}
