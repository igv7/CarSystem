package com.Igor.CarSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableMongoRepositories
//@EnableWebSecurity
public class CarSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarSystemApplication.class, args);
		System.out.println("Start");
	}

}
