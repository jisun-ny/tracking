package com.acorn.tracking.generator;

import java.io.FileNotFoundException;
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
            List<Products> products = readProductsFromJson(inputStream);
            insertProductsIntoDatabase(products);
        } catch (FileNotFoundException e) {
            handleFileNotFoundException(e);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private InputStream getProductsJsonInputStream() throws FileNotFoundException {
        InputStream inputStream = getClass().getResourceAsStream("/static/Products.json");
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: Products.json");
        }
        return inputStream;
    }

    private List<Products> readProductsFromJson(InputStream inputStream) throws IOException {
        return new GsonBuilder()
                .create().fromJson(
                        new InputStreamReader(inputStream),
                        new TypeToken<List<Products>>() {
                        }.getType());
    }

    private void insertProductsIntoDatabase(List<Products> products) {
        try {
            int batchSize = 100;
            for (int i = 0; i < products.size(); i += batchSize) {
                List<Products> batch = products.subList(i, Math.min(products.size(), i + batchSize));
                productsMapper.autoInsertProducts(batch);
            }
        } catch (DataAccessException e) {
            log.error("An error occurred while inserting products into the database", e);
            throw new RuntimeException("An error occurred while inserting products into the database", e);
        }
    }

    private void handleFileNotFoundException(FileNotFoundException e) {
        log.error("File not found: Products.json", e);
        throw new RuntimeException("File not found: Products.json", e);
    }

    private void handleIOException(IOException e) {
        log.error("An error occurred while reading the products from the JSON file", e);
        throw new RuntimeException("An error occurred while reading the products from the JSON file", e);
    }
}
