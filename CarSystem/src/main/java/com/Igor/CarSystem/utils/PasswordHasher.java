package com.Igor.CarSystem.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Prints the BCrypt hash of a password, for {@code carsystem.admin.password} in application.properties.
 * In Eclipse: Run As > Run Configurations > Arguments, enter the new password, then Run; copy the printed
 * line into the property.
 */
public class PasswordHasher {

	/** Prints the hash of the first program argument. */
	public static void main(String[] args) {
		if (args.length != 1 || args[0].isEmpty()) {
			System.err.println("Usage: PasswordHasher <password>");
			return;
		}
		System.out.println(new BCryptPasswordEncoder().encode(args[0]));
	}

}
