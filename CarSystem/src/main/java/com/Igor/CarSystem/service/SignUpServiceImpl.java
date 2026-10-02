package com.Igor.CarSystem.service;

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

	/**
	 * Saves the client.
	 * @return the saved client
	 * @throws Exception if the name is already taken or saving fails
	 */
	@Override
	public Client signUp(Client client) throws Exception {
		log.debug("************************StartSignUp************************");
		try {
			if (clientRepository.existsByName(client.getName())) {
				throw new Exception("This client name already exist in system, please try another name.");
			} else {
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
