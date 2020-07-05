package com.Igor.CarSystem.rest;

import java.util.Map;

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

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private Map<String, ClientSession> tokensMap;

	private ClientSession isActive(String token) {
		return tokensMap.get(token);
	}

	@Autowired
	private AdminServiceImpl adminServiceImpl;

	@Autowired
	private ClientReceiptServiceImpl clientReceiptServiceImpl;

	// Client Operations
	// Add Client
	@PostMapping("/addClient/{token}")
	public ResponseEntity<?> createClient(@RequestBody Client client, @PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.createClient(client), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to add client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Update Client
	@PutMapping("/updateClient/{token}/{id}")
	public ResponseEntity<?> updateClient(@RequestBody Client client, @PathVariable("token") String token,
			@PathVariable int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.updateClient(client), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to update client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View Client
	@GetMapping("/viewClient/{token}/{id}")
	public ResponseEntity<?> getClient(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getClientById(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Clients
	@GetMapping("/viewAllClients/{token}")
	public ResponseEntity<?> getAllClients(@PathVariable String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClients(), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all clients by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Delete Client
	@DeleteMapping("/deleteClient/{token}/{id}")
	public ResponseEntity<?> removeClient(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.deleteClient(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to delete client by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// ******************************************************************************************************************

	// Car Operations
	// Add Car
	@PostMapping("/addCar/{token}")
	public ResponseEntity<?> createCar(@RequestBody Car car, @PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.createCar(car), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to add car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Update Car
	@PutMapping("/updateCar/{token}/{id}")
	public ResponseEntity<?> updateCar(@RequestBody Car car, @PathVariable("token") String token,
			@PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.updateCar(car), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to update car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View Car By Id
	@GetMapping("/viewCar/{token}/{id}")
	public ResponseEntity<?> getCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getCarById(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View Car By CarNumber
	@GetMapping("/viewCarByNumber/{token}/{number}")
	public ResponseEntity<?> getCarByNumber(@PathVariable("token") String token,
			@PathVariable("number") String number) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getCarByNumber(number), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Cars
	@GetMapping("/viewAllCars/{token}")
	public ResponseEntity<?> getAllCars(@PathVariable String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllCars(), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all cars by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Delete Car
	@DeleteMapping("/deleteCar/{token}/{id}")
	public ResponseEntity<?> removeCar(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.deleteCar(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to delete car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View all Cars By CarType
	@GetMapping("/viewAllCarsByCarType/{token}/{type}")
	public ResponseEntity<?> getAllCarsByCarType(@PathVariable("token") String token,
			@PathVariable("type") CarType type) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllCarsByType(type), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Faild to get all cars by type!", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View all Cars By CarColor
	@GetMapping("/viewAllCarsByCarColor/{token}/{color}")
	public ResponseEntity<?> getAllCarsByCarColor(@PathVariable("token") String token,
			@PathVariable("color") CarColor color) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllCarsByColor(color), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Faild to get all cars by color!", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View Client Car By CarNumber
	@GetMapping("/viewClientCarByNumber/{token}/{id}/{number}")
	public ResponseEntity<?> getClientCarByNumber(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("number") String number) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getClientCarByNumber(id, number), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view Client car by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Client Cars
	@GetMapping("/viewAllClientCars/{token}/{id}")
	public ResponseEntity<?> getAllClientCars(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCars(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all Client cars by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Client Cars By CarType
	@GetMapping("/viewAllClientCarsByType/{token}/{id}/{type}")
	public ResponseEntity<?> getAllClientCarsByType(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("type") CarType type) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCarsByType(id, type), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all Client cars by type by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Client Cars By CarColor
	@GetMapping("/viewAllClientCarsByColor/{token}/{id}/{color}")
	public ResponseEntity<?> getAllClientCarsByColor(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("color") CarColor color) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCarsByColor(id, color), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all Client cars by color by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Client Cars By Price (until)
	@GetMapping("/viewAllClientCarsByPrice/{token}/{id}/{price}")
	public ResponseEntity<?> getAllClientCarsByPrice(@PathVariable("token") String token, @PathVariable("id") int id,
			@PathVariable("price") double price) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(adminServiceImpl.getAllClientCarsByPrice(id, price), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all Client cars by price by admin", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Receipts********************************************************************************************************
	// View Receipts By Client
	@GetMapping("/viewReceiptsByClient/{token}/{id}")
	public ResponseEntity<?> getReceiptsByClient(@PathVariable("token") String token, @PathVariable("id") int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientReceiptServiceImpl.getReceiptsByClient(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view Receipts By Client id: " + id, HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View All Receipts
	@GetMapping("/viewAllReceipts/{token}")
	public ResponseEntity<?> getAllReceipts(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientReceiptServiceImpl.getAllReceipts(), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all Receipts ", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

}
