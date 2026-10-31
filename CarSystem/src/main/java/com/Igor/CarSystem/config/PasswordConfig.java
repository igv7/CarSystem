package com.Igor.CarSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Password hashing for client accounts. */
@Configuration
public class PasswordConfig {

	/**
	 * BCrypt: {@code encode()} turns a typed password into a salted hash for storage, {@code matches()} checks a
	 * typed password against a stored hash at sign-in.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
