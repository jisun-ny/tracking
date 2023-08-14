package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.Products;

@Mapper
public interface ProductsMapper {
    void autoInsertProducts(Products product);
}
