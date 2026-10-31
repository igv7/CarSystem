package com.Igor.CarSystem.rest;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.CarSystem.enums.ClientType;
import com.Igor.CarSystem.service.AdminServiceImpl;
import com.Igor.CarSystem.service.CarSystem;
import com.Igor.CarSystem.service.ClientServiceImpl;
import com.Igor.CarSystem.service.Facade;
import com.Igor.CarSystem.task.ClientSession;



/** Login endpoint that issues session tokens for both admins and clients, and checks whether a token is still valid. */
@RestController
@RequestMapping("/carSystem")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private Map<String, ClientSession> tokensMap;
	
	@Autowired
	private CarSystem carSystem;
	
	
	/**
	 * POST /carSystem/login. Checks the credentials and, on success, stores a new session
	 * under a random token and returns it.
	 *
	 * @param userName admin or client name
	 * @param password password
	 * @param type a {@link ClientType} name: "ADMIN" or "CLIENT"
	 * @return 200 with the token, or 401 with the reason
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String userName, @RequestParam String password, @RequestParam String type) {
		ClientType clientType;
		try {
			clientType = ClientType.valueOf(type);
		} catch (IllegalArgumentException e) {
			log.info("Login rejected for user '{}': wrong type '{}'", userName, type);
			return new ResponseEntity<>("Wrong type", HttpStatus.UNAUTHORIZED);
		}
		ClientSession clientSession = new ClientSession();
		Facade facade = null;
		String token = UUID.randomUUID().toString();
		long LastAccsessed = System.currentTimeMillis();
		try {
			facade = carSystem.login(userName, password, clientType);
			clientSession.setFacade(facade);
			clientSession.setLastAccessed(LastAccsessed);
			tokensMap.put(token, clientSession);
			log.debug("Session created for {} user '{}' ({} active sessions)", type, userName, tokensMap.size());
			return new ResponseEntity<>(token, HttpStatus.OK);
		} catch (Exception e) {
			log.info("Login failed for {} user '{}': {}", type, userName, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	

	/**
	 * GET /carSystem/checkSession/{token}/{type}. Lets the frontend find out, before opening the admin or
	 * client pages, whether its saved token is still valid: the session may have timed out or been lost in a
	 * backend restart. The frontend calls it on a timer while a user is signed in, so it does not count as
	 * activity: it leaves the session's last-accessed time unchanged, and an idle session still times out.
	 *
	 * @param token the session token
	 * @param type a {@link ClientType} name: "ADMIN" or "CLIENT"; the session must be of this type
	 * @return 200 if the session is active and of this type, otherwise 401
	 */
	@GetMapping("/checkSession/{token}/{type}")
	public ResponseEntity<?> checkSession(@PathVariable("token") String token, @PathVariable("type") String type) {
		ClientType clientType;
		try {
			clientType = ClientType.valueOf(type);
		} catch (IllegalArgumentException e) {
			log.info("Session check rejected: wrong type '{}'", type);
			return new ResponseEntity<>("Wrong type", HttpStatus.UNAUTHORIZED);
		}
		ClientSession clientSession = tokensMap.get(token);
		Facade facade = clientSession == null ? null : clientSession.getFacade();
		boolean matches = clientType == ClientType.ADMIN ? facade instanceof AdminServiceImpl : facade instanceof ClientServiceImpl;
		if (matches) {
			log.debug("Session check: {} session is active", clientType);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		log.info("Session check: no active {} session for this token", clientType);
		return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
	}

}
