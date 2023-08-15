package com.acorn.tracking.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acorn.tracking.domain.Admins;
import com.acorn.tracking.mapper.AdminsMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminsGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AdminsGenerator.class);

    private final AdminsMapper adminsMapper;

    public void insertAdmins() {
        try {
            Admins admins = Admins.builder()
                    .name("admin")
                    .email("admin@gmail.com")
                    .password("1234")
                    .build();
            adminsMapper.autoInsertAdmins(admins);
        } catch (Exception e) {
            logger.error("An error occurred while inserting admins", e);
        }
    }
}
