package com.Igor.CarSystem.service;

import com.Igor.CarSystem.model.Client;

/** Self-registration of new clients. */
public interface SignUpService {
	
	/** Registers a new client; fails if the name or identity number is taken. */
	public Client signUp(Client client) throws Exception;

}
