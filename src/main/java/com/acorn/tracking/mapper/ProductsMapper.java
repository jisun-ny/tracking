package com.acorn.tracking.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.Products;

@Mapper
public interface ProductsMapper {
    List<Products> getRandomProducts(int count);
    void inventoryReduction(int product_id, int sale);
    void autoInsertProducts(List<Products> products);
    String getCategoryByProductsId(int product_id);
}
