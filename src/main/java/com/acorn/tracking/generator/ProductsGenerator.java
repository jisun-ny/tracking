package com.acorn.tracking.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acorn.tracking.domain.Products;
import com.acorn.tracking.mapper.ProductsMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductsGenerator {

    private final ProductsMapper productsMapper;

    @Transactional
    public void loadProductsFromFile() {
        try (InputStream inputStream = getProductsJsonInputStream()) {
            insertProductsIntoDatabase(readProductsFromJson(inputStream));
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private InputStream getProductsJsonInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/static/Products.json");
        if (inputStream == null) {
            throw new IOException("File not found: Products.json");
        }
        return inputStream;
    }

    private List<Products> readProductsFromJson(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return new GsonBuilder().create().fromJson(reader, new TypeToken<List<Products>>() {
            }.getType());
        }
    }

    private void insertProductsIntoDatabase(List<Products> products) {
        try {
            for (int i = 0; i < products.size(); i += 100) {
                productsMapper.autoInsertProducts(products.subList(i, Math.min(products.size(), i + 100)));
            }
        } catch (DataAccessException e) {
            handleDatabaseException(e);
        }
    }

    private void handleDatabaseException(DataAccessException e) {
        log.error("An error occurred while inserting products into the database", e);
        throw new RuntimeException("An error occurred while inserting products into the database", e);
    }

    private void handleIOException(IOException e) {
        log.error("An error occurred while reading the products from the JSON file", e);
        throw new RuntimeException("An error occurred while reading the products from the JSON file", e);
    }
}
