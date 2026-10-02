package com.Igor.CarSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point of the Car System REST backend. Enables JPA repositories (PostgreSQL),
 * Mongo repositories (MongoDB) and the scheduled billing job.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableMongoRepositories
@EnableScheduling
//@EnableWebSecurity
public class CarSystemApplication {

	private static final Logger log = LoggerFactory.getLogger(CarSystemApplication.class);

	/** Starts the Spring Boot application on port 8080. */
	public static void main(String[] args) {
		SpringApplication.run(CarSystemApplication.class, args);
		log.info("Start");
	}

}
