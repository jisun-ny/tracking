package com.acorn.tracking.service;

import java.util.List;

import com.acorn.tracking.domain.Products;

public interface OrderDetailsService {
    void autoInsertOrderDetails(int orders_id, List<Products> randomProducts);
}
