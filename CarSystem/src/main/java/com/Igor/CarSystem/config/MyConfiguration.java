package com.Igor.CarSystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** CORS setup for the Angular frontend. */
@SuppressWarnings("deprecation")
@Configuration
public class MyConfiguration {

	/** The only browser origin allowed to call the API ({@code carsystem.cors.allowed-origin}). */
	@Value("${carsystem.cors.allowed-origin}")
	private String allowedOrigin;

	/** Allows all HTTP methods, with credentials, from {@link #allowedOrigin} only. */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
				.addMapping("/**")
				.allowedOrigins(allowedOrigin)
				.allowedMethods("*").allowCredentials(true);
			}
		};
	}

}