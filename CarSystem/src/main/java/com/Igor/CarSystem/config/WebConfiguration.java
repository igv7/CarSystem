package com.Igor.CarSystem.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.Igor.CarSystem.task.ClientSession;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Swagger setup and the shared session store. Swagger UI: http://localhost:8080/swagger-ui.html */
@Configuration
@EnableSwagger2
public class WebConfiguration {
	
//  http://localhost:8080/swagger-ui.html#/
	
	/**
	 * In-memory session store shared by all controllers: login token -> {@link ClientSession}.
	 * Filled by {@code LoginController}, cleaned up by {@code SessionTimeout}. Sessions are lost on restart.
	 * Concurrent, because request threads and the cleanup thread use it at the same time.
	 */
	@Bean
	public Map<String, ClientSession> tokensMap() {
		return new ConcurrentHashMap<String, ClientSession>();
	}
	
	/**
	 * Swagger docket that documents every controller and path.
	 * Note: this method has no {@code @Bean}, so Spring never registers it and Swagger runs with its defaults.
	 */
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
				
	}

}
