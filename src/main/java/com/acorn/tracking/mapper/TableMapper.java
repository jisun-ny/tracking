package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TableMapper {
    void dropOrderDetails();
    void dropRecalls();
    void dropBaskets();
    void dropDeliveries();
    void dropOrders();
    void dropProducts();
    void dropAdmins();

    void createAdmins();
    void createProducts();
    void createOrders();
    void createDeliveries();
    void createBaskets();
    void createRecalls();
    void createOrderDetails();
}
