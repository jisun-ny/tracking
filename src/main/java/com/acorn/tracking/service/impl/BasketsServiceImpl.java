package com.acorn.tracking.service.impl;

import java.math.BigDecimal;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.Baskets;
import com.acorn.tracking.mapper.BasketsMapper;
import com.acorn.tracking.mapper.ProductsMapper;
import com.acorn.tracking.service.BasketsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketsServiceImpl implements BasketsService {

    private static final Logger logger = LoggerFactory.getLogger(BasketsServiceImpl.class);
    private static final Random random = new Random();

    private final ProductsMapper productsMapper;
    private final BasketsMapper basketsMapper;

    private double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    @Override
    @Transactional
    public void autoInsertBaskets(int product_id, int delivery_id) {
        try {
            String category = productsMapper.getCategoryByProductsId(product_id);
            Baskets basket;

            switch (category) {
                case "야채/과일":
                    basket = createBasket(product_id, delivery_id, 4, 6, 58, 62, 88, 92);
                    break;
                case "육류":
                    basket = createBasket(product_id, delivery_id, 0, 4, 48, 52, 83, 87);
                    break;
                case "수산물":
                    basket = createBasket(product_id, delivery_id, -2, 2, 53, 57, 78, 82);
                    break;
                case "유제품":
                    basket = createBasket(product_id, delivery_id, 2, 6, 63, 67, 73, 77);
                    break;
                default:
                    basket = createBasket(product_id, delivery_id, 18, 22, 38, 42, 73, 77);
                    break;
            }
            basketsMapper.autoInsertBaskets(basket);
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    private Baskets createBasket(int product_id, int delivery_id, double tempMin, double tempMax, double humidMin,
            double humidMax, double freshMin, double freshMax) {
        return Baskets.builder()
                .product_id(product_id)
                .delivery_id(delivery_id)
                .temperature(BigDecimal.valueOf(randomDouble(tempMin, tempMax)))
                .humidity(BigDecimal.valueOf(randomDouble(humidMin, humidMax)))
                .freshness(BigDecimal.valueOf(randomDouble(freshMin, freshMax)))
                .build();
    }

    private void handleNumberFormatException(NumberFormatException e) {
        logger.error("Number format exception occurred while creating basket object", e);
        throw new IllegalArgumentException("Invalid number format in basket details", e);
    }

    private void handleGeneralException(Exception e) {
        logger.error("An unexpected error occurred while inserting basket", e);
        throw new RuntimeException("Unexpected error inserting basket", e);
    }
}
