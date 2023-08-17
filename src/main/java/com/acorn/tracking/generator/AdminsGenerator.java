package com.acorn.tracking.generator;

import org.springframework.stereotype.Component;
import org.springframework.dao.DataAccessException;

import com.acorn.tracking.domain.Admins;
import com.acorn.tracking.mapper.AdminsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminsGenerator {

    private final AdminsMapper adminsMapper;

    public void insertAdmins() {
        try {
            Admins admins = Admins.builder()
                    .name("admin")
                    .email("admin@gmail.com")
                    .password("1234")
                    .build();
            adminsMapper.autoInsertAdmins(admins);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    private void handleDataAccessException(DataAccessException e) {
        log.error("An error occurred during data access.", e);
        throw new RuntimeException("Error during data access", e);
    }

    private void handleGeneralException(Exception e) {
        log.error("An error occurred while inserting an admin.", e);
        throw new RuntimeException("Unexpected error during admin insertion", e);
    }
}
