package com.Igor.CarSystem.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.CarSystem.service.ClientServiceImpl;
import com.Igor.CarSystem.task.ClientSession;

/**
 * Endpoints for a logged-in client. Every endpoint takes the client's session token in the path;
 * the session holds that client's own {@link ClientServiceImpl}, so each call acts on the right client.
 */
@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private Map<String, ClientSession> tokensMap;

	/**
	 * Finds the client session for a token.
	 * @return the session, or null if the token is unknown, expired or belongs to an admin
	 */
	private ClientSession isActive(String token) {
		ClientSession clientSession = tokensMap.get(token);
		if (clientSession != null && clientSession.getFacade() instanceof ClientServiceImpl) {
			return clientSession;
		}
		log.info("Rejected client request: token is unknown, expired or not a client session");
		return null;
	}


	/**
	 * POST /client/addCar/{token}/{id}. Rents car {@code id}: charges its price and writes a receipt.
	 * @return 200 with the car, or 400 if the balance is negative, the car is already rented
	 */
	@PostMapping("/addCar/{token}/{id}")
	public ResponseEntity<?> getCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.getCar(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to get Car. Car id: {}: {}", id, e.getMessage());
				return new ResponseEntity<>("You have no money on your account! Failed to get Car. Car id: " + id,
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /client/viewCars/{token}. All cars in the system. */
	@GetMapping("/viewCars/{token}")
	public ResponseEntity<?> getCars(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.getCars(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view cars by client" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view cars by client", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /client/viewMyCars/{token}. Cars this client currently rents. */
	@GetMapping("/viewMyCars/{token}")
	public ResponseEntity<?> getMyCars(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.getMyCars(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view my cars" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view my cars", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** DELETE /client/returnCar/{token}/{id}. Returns one of this client's rented cars and marks it available; 400 if the client doesn't rent it. */
	@DeleteMapping("/returnCar/{token}/{id}")
	public ResponseEntity<?> returnCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.returnCar(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to return car" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to return car", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /client/viewMyReceipts/{token}. This client's receipts. */
	@GetMapping("/viewMyReceipts/{token}")
	public ResponseEntity<?> getReceiptsByClient(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.getReceiptsByClient(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view Receipts By Client. " + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view Receipts By Client. ", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /client/viewBalance/{token}. This client's current balance. */
	@GetMapping("/viewBalance/{token}")
	public ResponseEntity<?> getBalance(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.getBalance(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view balance" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view balance", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** DELETE /client/deleteAccount/{token}. Deletes this client and marks their rented cars available again; the cars stay in the catalogue. */
	@DeleteMapping("/deleteAccount/{token}")
	public ResponseEntity<?> deleteAccount(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			ClientServiceImpl clientServiceImpl = (ClientServiceImpl) clientSession.getFacade();
			try {
				return new ResponseEntity<>(clientServiceImpl.deleteAccount(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed remove account." + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed remove account.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

}
