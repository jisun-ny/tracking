package com.acorn.tracking.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.Products;

@Mapper
public interface ProductsMapper {
    void autoInsertProducts(Products product);
    List<Products> getRandomProducts(int count);
    void inventoryReduction(int product_id, int sale);
}
