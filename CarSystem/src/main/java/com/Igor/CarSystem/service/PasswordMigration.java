package com.Igor.CarSystem.service;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.repo.ClientRepository;

/**
 * Runs at startup and hashes client passwords still stored as plain text (saved before hashing was added).
 * Values that already are BCrypt hashes are left alone, so later runs change nothing.
 */
@Component
public class PasswordMigration implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(PasswordMigration.class);

	/** A BCrypt hash: $2a$, $2b$ or $2y$, a two-digit cost, $, then 53 characters of salt and hash. */
	private static final Pattern BCRYPT_HASH = Pattern.compile("\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}");

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/** Hashes every plain-text password in the client table and logs how many were changed. */
	@Override
	public void run(ApplicationArguments args) {
		int hashed = 0;
		for (Client client : clientRepository.findAll()) {
			String password = client.getPassword();
			if (password != null && !isHashed(password)) {
				clientRepository.updatePassword(client.getId(), passwordEncoder.encode(password));
				hashed++;
			}
		}
		if (hashed > 0) {
			log.info("Hashed the plain-text passwords of {} client(s)", hashed);
		}
	}

	/** Whether a stored password already is a BCrypt hash. */
	static boolean isHashed(String password) {
		return BCRYPT_HASH.matcher(password).matches();
	}

}
