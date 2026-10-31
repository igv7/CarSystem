package com.Igor.CarSystem.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Checks that the startup migration tells plain-text passwords from BCrypt hashes, and that the admin password is stored hashed. */
class PasswordMigrationTest {

	@Test
	void hashesAreRecognisedAndPlainTextIsNot() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hash = encoder.encode("secret");
		assertTrue(PasswordMigration.isHashed(hash));
		assertTrue(encoder.matches("secret", hash));
		assertFalse(PasswordMigration.isHashed("secret"));
		assertFalse(PasswordMigration.isHashed("1234"));
		assertFalse(PasswordMigration.isHashed("$2a$10$tooShort"));
	}

	@Test
	void adminPasswordInPropertiesIsAHash() throws Exception {
		Properties properties = new Properties();
		try (InputStream in = getClass().getResourceAsStream("/application.properties")) {
			properties.load(in);
		}
		assertTrue(PasswordMigration.isHashed(properties.getProperty("carsystem.admin.password")));
	}

}
