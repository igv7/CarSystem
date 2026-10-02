package com.Igor.CarSystem.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.enums.ClientType;
import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.repo.ClientRepository;
import com.Igor.CarSystem.task.SessionTimeout;

/** Application core: starts and stops the session-timeout thread and authenticates logins. */
@Service
public class CarSystem {

	private static final Logger log = LoggerFactory.getLogger(CarSystem.class);
	
//	@Autowired
//	private ConfigurableApplicationContext context;
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private AdminServiceImpl adminServiceImpl;
	
	@Autowired
	private SessionTimeout sessionTask;
	
	@Autowired
	private ClientRepository clientRepository;

		

	/** Runs at startup: starts the session-timeout thread. */
	@PostConstruct
	public void init() {
		log.info("Welcome to the Car System!");
		log.info("Session Timeout Task in ACTION...");
		sessionTask.start();
	}

	/** Runs at shutdown: stops the session-timeout thread. */
	@PreDestroy
	public void destroy() {
		log.info("The Car System is shut down.");
		sessionTask.stop();
//		context.close();
	}

	/**
	 * Checks credentials and returns the service the user will work with.
	 * The admin account is hard-coded as admin / 1234. A client gets a new {@link ClientServiceImpl}
	 * bound to their ID.
	 *
	 * @return {@link AdminServiceImpl} for the admin, a new {@link ClientServiceImpl} for a client
	 * @throws Exception if the credentials are wrong
	 */
	public Facade login(String userName, String password, ClientType type) throws Exception {
		switch (type) {
		case ADMIN:
			if (userName.equals("admin") && password.equals("1234")) {
				log.info("Welcome Admin! You're logged into The Car System");
				return adminServiceImpl;
			}
			break;
		case CLIENT:
			Client client = clientRepository.findByNameAndPassword(userName, password);
			if (client != null) {
				ClientServiceImpl clientServiceImpl = context.getBean(ClientServiceImpl .class);
				clientServiceImpl.setClientId(client.getId());
				log.info("Welcome " +client.getName()+ "! You're logged into The Car System");
				return clientServiceImpl;
			}
		
		}
		throw new Exception("Client not found. Check your data");
	}

}
