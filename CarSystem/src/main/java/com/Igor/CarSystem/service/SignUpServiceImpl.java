package com.Igor.CarSystem.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.repo.ClientRepository;



/** Self-registration of new clients. Unlike the admin's createClient, no fields are required. */
@Service
public class SignUpServiceImpl implements SignUpService, Facade {

	private static final Logger log = LoggerFactory.getLogger(SignUpServiceImpl.class);
	
	@Autowired
	private ClientRepository clientRepository;

	/** Highest starting balance a client may give themselves at sign-up (the administration's gift). */
	private static final double MAX_SIGN_UP_BALANCE = 100;

	/**
	 * Saves the client as a new account. Field rules are checked by {@code @Valid} in the controller; this
	 * also caps the starting balance and ignores any id or cars sent in the body, so sign-up can't
	 * overwrite an existing client or take over rented cars.
	 * @return the saved client
	 * @throws Exception if the name is already taken, the balance is above the sign-up limit, or saving fails
	 */
	@Override
	public Client signUp(Client client) throws Exception {
		log.debug("************************StartSignUp************************");
		try {
			if (client.getBalance() > MAX_SIGN_UP_BALANCE) {
				throw new Exception("The starting balance can be at most " + (int) MAX_SIGN_UP_BALANCE + ".");
			} else if (clientRepository.existsByName(client.getName())) {
				throw new Exception("This client name already exist in system, please try another name.");
			} else {
				client.setId(0);
				client.setCars(new ArrayList<>());
				clientRepository.save(client);
				log.info("Success on sign up: " + client.getName() + " -> " +client); 
				log.debug("************************EndSignUp************************");
			}
		} catch (Exception e) {
			log.error("Failed on sign up: {}", e.getMessage());
			throw new Exception("Failed on sign up! " + e.getMessage());
		}
		return client;
	}

}
