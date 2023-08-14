package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.OrderDetails;

@Mapper
public interface OrderDetailsMapper {
    void autoInsertOrderDetails(OrderDetails orderDetails);
}
