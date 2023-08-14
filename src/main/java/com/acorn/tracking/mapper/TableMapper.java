package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TableMapper {
    void dropAdmins();
    void createAdmins();
    void dropProducts();
    void createProducts();
    void dropBaskets();
    void createBaskets();
    void dropOrders();
    void createOrders();
    void dropOrderDetails(); 
    void createOrderDetails(); 
    void dropDeliveries();
    void createDeliveries();
    void dropRecalls();
    void createRecalls();
}
