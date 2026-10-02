package com.Igor.CarSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableMongoRepositories
@EnableScheduling
//@EnableWebSecurity
public class CarSystemApplication {

	private static final Logger log = LoggerFactory.getLogger(CarSystemApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarSystemApplication.class, args);
		log.info("Start");
	}

}
