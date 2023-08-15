package com.acorn.tracking.generator;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.Deliveries;
import com.acorn.tracking.mapper.DeliveriesMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveriesGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DeliveriesGenerator.class);

    private final DeliveriesMapper deliveriesMapper;
    private final BasketsGenerator basketsGenerator;

    @Transactional
    public void autoInsertDeliveries(int order_id, int product_id) {
        try {
            deliveriesMapper.autoInsertDeliveries(createDeliveries(order_id));
            basketsGenerator.autoInsertBaskets(product_id, deliveriesMapper.getLastInsertDeliveriesId());
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    private Deliveries createDeliveries(int order_id) {
        return Deliveries.builder()
                .order_id(order_id)
                .delivery_status("배송중")
                .latitude(new BigDecimal(37.52318))
                .longitude(new BigDecimal(126.95853))
                .build();
    }

    private void handleNumberFormatException(NumberFormatException e) {
        logger.error("Number format exception occurred while creating deliveries object", e);
        throw new IllegalArgumentException("Invalid number format in deliveries details", e);
    }

    private void handleGeneralException(Exception e) {
        logger.error("An unexpected error occurred while inserting deliveries", e);
        throw new RuntimeException("Unexpected error inserting deliveries", e);
    }
}
