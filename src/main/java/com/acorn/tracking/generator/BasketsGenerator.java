package com.acorn.tracking.generator;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.Baskets;
import com.acorn.tracking.mapper.BasketsMapper;
import com.acorn.tracking.mapper.ProductsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasketsGenerator {

    private static final Random random = new Random();

    private final ProductsMapper productsMapper;
    private final BasketsMapper basketsMapper;

    private double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    @Transactional
    public void autoInsertBaskets(int productId, int deliveryId) {
        try {
            double tempMin, tempMax, humidMin, humidMax, freshMin, freshMax;
            switch (productsMapper.getCategoryByProductsId(productId)) {
                case "야채/과일":
                    tempMin = 4;
                    tempMax = 6;
                    humidMin = 58;
                    humidMax = 62;
                    freshMin = 88;
                    freshMax = 92;
                    break;
                case "육류":
                    tempMin = 0;
                    tempMax = 4;
                    humidMin = 48;
                    humidMax = 52;
                    freshMin = 83;
                    freshMax = 87;
                    break;
                case "수산물":
                    tempMin = -2;
                    tempMax = 2;
                    humidMin = 53;
                    humidMax = 57;
                    freshMin = 78;
                    freshMax = 82;
                    break;
                case "유제품":
                    tempMin = 2;
                    tempMax = 6;
                    humidMin = 63;
                    humidMax = 67;
                    freshMin = 73;
                    freshMax = 77;
                    break;
                default:
                    tempMin = 2;
                    tempMax = 5;
                    humidMin = 38;
                    humidMax = 42;
                    freshMin = 73;
                    freshMax = 77;
                    break;
            }

            basketsMapper.autoInsertBaskets(createBasket(productId, deliveryId, tempMin, tempMax, humidMin, humidMax, freshMin, freshMax));
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    private Baskets createBasket(int productId, int deliveryId, double tempMin, double tempMax, double humidMin,
            double humidMax, double freshMin, double freshMax) {
        return Baskets.builder()
                .product_id(productId)
                .delivery_id(deliveryId)
                .temperature(BigDecimal.valueOf(randomDouble(tempMin, tempMax)))
                .humidity(BigDecimal.valueOf(randomDouble(humidMin, humidMax)))
                .freshness(BigDecimal.valueOf(randomDouble(freshMin, freshMax)))
                .build();
    }

    private void handleNumberFormatException(NumberFormatException e) {
        log.error("Number format exception occurred while creating basket object", e);
        throw new IllegalArgumentException("Invalid number format in basket details", e);
    }

    private void handleGeneralException(Exception e) {
        log.error("An unexpected error occurred while inserting basket", e);
        throw new RuntimeException("Unexpected error inserting basket", e);
    }
}
