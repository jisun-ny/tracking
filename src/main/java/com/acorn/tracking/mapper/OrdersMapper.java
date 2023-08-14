package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.Orders;

@Mapper
public interface OrdersMapper {
    void autoInsertOrders(Orders orders);
    int getLastInsertOrderId();
}
