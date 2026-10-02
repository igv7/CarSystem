package com.Igor.CarSystem.service;

import com.Igor.CarSystem.model.Client;

/** Self-registration of new clients. */
public interface SignUpService {
	
	/** Registers a new client; fails if the name is taken. */
	public Client signUp(Client client) throws Exception;

}
