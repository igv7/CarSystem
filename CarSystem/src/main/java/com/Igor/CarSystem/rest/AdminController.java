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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.service.AdminServiceImpl;
import com.Igor.CarSystem.service.ClientReceiptServiceImpl;
import com.Igor.CarSystem.task.ClientSession;

/**
 * Admin endpoints for managing clients, cars and receipts.
 * Every endpoint takes an admin session token in the path; an unknown, expired or client token gets 401.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

	private static final Logger log = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private Map<String, ClientSession> tokensMap;

	/**
	 * Finds the admin session for a token.
	 * @return the session, or null if the token is unknown, expired or belongs to a client
	 */
	private ClientSession isActive(String token) {
		ClientSession clientSession = tokensMap.get(token);
		if (clientSession != null && clientSession.getFacade() instanceof AdminServiceImpl) {
			return clientSession;
		}
		log.info("Rejected admin request: token is unknown, expired or not an admin session");
		return null;
	}

	@Autowired
	private AdminServiceImpl adminServiceImpl;

	@Autowired
	private ClientReceiptServiceImpl clientReceiptServiceImpl;

	// Client Operations
	/**
	 * POST /admin/addClient/{token}. Creates a client from the JSON body.
	 * @return 200 with the client (not saved if required fields are missing), or 400 if the name is taken
	 */
	@PostMapping("/addClient/{token}")
	public ResponseEntity<?> createClient(@RequestBody Client client, @PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.createClient(client), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to add client by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to add client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * PUT /admin/updateClient/{token}/{id}. Overwrites a client's details with the JSON body.
	 * The client is identified by the {@code id} inside the body; the {@code id} path variable is not used.
	 */
	@PutMapping("/updateClient/{token}/{id}")
	public ResponseEntity<?> updateClient(@RequestBody Client client, @PathVariable("token") String token,
			@PathVariable int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.updateClient(client), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to update client by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to update client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewClient/{token}/{id}. One client, or 400 if it doesn't exist. */
	@GetMapping("/viewClient/{token}/{id}")
	public ResponseEntity<?> getClient(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getClientById(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view client by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllClients/{token}. All clients, or 400 if there are none. */
	@GetMapping("/viewAllClients/{token}")
	public ResponseEntity<?> getAllClients(@PathVariable String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClients(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all clients by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all clients by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** DELETE /admin/deleteClient/{token}/{id}. Deletes a client and marks their rented cars available again; the cars stay in the catalogue. */
	@DeleteMapping("/deleteClient/{token}/{id}")
	public ResponseEntity<?> removeClient(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.deleteClient(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to delete client by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to delete client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// ******************************************************************************************************************

	// Car Operations
	/**
	 * POST /admin/addCar/{token}. Creates a car from the JSON body.
	 * @return 200 with the car (always created available, amount = 1; not saved if a field is missing or the price is 0), or 400 if the number is taken
	 */
	@PostMapping("/addCar/{token}")
	public ResponseEntity<?> createCar(@RequestBody Car car, @PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.createCar(car), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to add car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to add car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * PUT /admin/updateCar/{token}/{id}. Overwrites a car's details with the JSON body.
	 * The car is identified by the {@code id} inside the body; the {@code id} path variable is not used.
	 */
	@PutMapping("/updateCar/{token}/{id}")
	public ResponseEntity<?> updateCar(@RequestBody Car car, @PathVariable("token") String token,
			@PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.updateCar(car), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to update car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to update car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewCar/{token}/{id}. One car by ID, or 400 if it doesn't exist. */
	@GetMapping("/viewCar/{token}/{id}")
	public ResponseEntity<?> getCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getCarById(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewCarByNumber/{token}/{number}. One car by number, or 400 if it doesn't exist. */
	@GetMapping("/viewCarByNumber/{token}/{number}")
	public ResponseEntity<?> getCarByNumber(@PathVariable("token") String token,
			@PathVariable("number") String number) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getCarByNumber(number), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllCars/{token}. All cars, or 400 if there are none. */
	@GetMapping("/viewAllCars/{token}")
	public ResponseEntity<?> getAllCars(@PathVariable String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllCars(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all cars by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all cars by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** DELETE /admin/deleteCar/{token}/{id}. Deletes a car, unlinking it from the client renting it. */
	@DeleteMapping("/deleteCar/{token}/{id}")
	public ResponseEntity<?> removeCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.deleteCar(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to delete car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to delete car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** DELETE /admin/returnCar/{token}/{id}. Returns a rented car on the client's behalf and marks it available (amount = 1). */
	@DeleteMapping("/returnCar/{token}/{id}")
	public ResponseEntity<?> returnCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.returnCar(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to return car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to return car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllCarsByCarType/{token}/{type}. All cars of one make. */
	@GetMapping("/viewAllCarsByCarType/{token}/{type}")
	public ResponseEntity<?> getAllCarsByCarType(@PathVariable("token") String token,
			@PathVariable("type") CarType type) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllCarsByType(type), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Faild to get all cars by type!" + ": {}", e.getMessage());
				return new ResponseEntity<>("Faild to get all cars by type!", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllCarsByCarColor/{token}/{color}. All cars of one color. */
	@GetMapping("/viewAllCarsByCarColor/{token}/{color}")
	public ResponseEntity<?> getAllCarsByCarColor(@PathVariable("token") String token,
			@PathVariable("color") CarColor color) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllCarsByColor(color), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Faild to get all cars by color!" + ": {}", e.getMessage());
				return new ResponseEntity<>("Faild to get all cars by color!", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewClientCarByNumber/{token}/{id}/{number}. A car by number, if client {@code id} rents any cars. */
	@GetMapping("/viewClientCarByNumber/{token}/{id}/{number}")
	public ResponseEntity<?> getClientCarByNumber(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("number") String number) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getClientCarByNumber(id, number), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view Client car by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view Client car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllClientCars/{token}/{id}. All cars rented by client {@code id}. */
	@GetMapping("/viewAllClientCars/{token}/{id}")
	public ResponseEntity<?> getAllClientCars(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCars(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all Client cars by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all Client cars by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllClientCarsByType/{token}/{id}/{type}. Cars of one make rented by client {@code id}. */
	@GetMapping("/viewAllClientCarsByType/{token}/{id}/{type}")
	public ResponseEntity<?> getAllClientCarsByType(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("type") CarType type) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCarsByType(id, type), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all Client cars by type by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all Client cars by type by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllClientCarsByColor/{token}/{id}/{color}. Cars of one color rented by client {@code id}. */
	@GetMapping("/viewAllClientCarsByColor/{token}/{id}/{color}")
	public ResponseEntity<?> getAllClientCarsByColor(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("color") CarColor color) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCarsByColor(id, color), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all Client cars by color by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all Client cars by color by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllClientCarsByPrice/{token}/{id}/{price}. Cars rented by client {@code id} costing at most {@code price}. */
	@GetMapping("/viewAllClientCarsByPrice/{token}/{id}/{price}")
	public ResponseEntity<?> getAllClientCarsByPrice(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("price") double price) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCarsByPrice(id, price), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all Client cars by price by admin" + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all Client cars by price by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Receipts********************************************************************************************************
	/** GET /admin/viewReceiptsByClient/{token}/{id}. All receipts of client {@code id}. */
	@GetMapping("/viewReceiptsByClient/{token}/{id}")
	public ResponseEntity<?> getReceiptsByClient(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientReceiptServiceImpl.getReceiptsByClient(id), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view Receipts By Client id: " + id + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view Receipts By Client id: " + id, HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	/** GET /admin/viewAllReceipts/{token}. All receipts of all clients. */
	@GetMapping("/viewAllReceipts/{token}")
	public ResponseEntity<?> getAllReceipts(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientReceiptServiceImpl.getAllReceipts(), HttpStatus.OK);
			} catch (Exception e) {
				log.error("Failed to view all Receipts " + ": {}", e.getMessage());
				return new ResponseEntity<>("Failed to view all Receipts ", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

}
