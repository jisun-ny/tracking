package com.acorn.tracking.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acorn.tracking.domain.Baskets;

@Mapper
public interface BasketsMapper {
    void autoInsertBaskets(Baskets baskets);
}
