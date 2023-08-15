package com.acorn.tracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.acorn.tracking.generator.AdminsGenerator;
import com.acorn.tracking.generator.ProductsGenerator;
import com.acorn.tracking.generator.TableGenerator;

@SpringBootApplication
@EnableScheduling
public class TrackingApplication {

	private static final Logger logger = LoggerFactory.getLogger(TrackingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TrackingApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(TableGenerator tableGenerator, ProductsGenerator productsGenerator, AdminsGenerator adminsGenerator) {
		return args -> {
			logger.info("Initializing database");
			tableGenerator.resetDatabase();
			logger.info("Database initialized successfully");
			logger.info("Generating products");
			productsGenerator.loadProductsFromFile();
			logger.info("Products generated successfully");
			logger.info("Generating Admins");
			adminsGenerator.insertAdmins();
			logger.info("Admins generated successfully");
		};
	}
}
