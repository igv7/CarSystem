package com.Igor.Tal.CarSystem.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.Tal.CarSystem.service.ClientReceiptServiceImpl;
import com.Igor.Tal.CarSystem.service.ClientServiceImpl;
import com.Igor.Tal.CarSystem.task.ClientSession;

@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private Map<String, ClientSession> tokensMap;

	private ClientSession isActive(String token) {
		return tokensMap.get(token);
	}

	@Autowired
	private ClientServiceImpl clientServiceImpl;

	@Autowired
	private ClientReceiptServiceImpl clientReceiptServiceImpl;

	// Add Car
	@PostMapping("/addCar/{token}/{id}")
	public ResponseEntity<?> getCar(@PathVariable("token") String token, @PathVariable int id) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientServiceImpl.getCar(id), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("You have no money on your account! Failed to get Car. Car id: " + id,
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

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

	// View All Client Receipts By ReceiptDate (until)
	@GetMapping("/viewAllClientReceiptsByReceiptDate/{token}/{receiptDate}")
	public ResponseEntity<?> getAllClientReceiptsByReceiptDate(@PathVariable("token") String token,
			@PathVariable("receiptDate") String receiptDate) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientServiceImpl.getAllClientReceiptsByDate(receiptDate), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view all Client Receipts by receiptDate!",
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// View Balance
	@GetMapping("/viewBalance/{token}")
	public ResponseEntity<?> getBalance(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientServiceImpl.getBalance(), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed to view balance", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

	// Delete Account
	@DeleteMapping("/deleteAccount/{token}")
	public ResponseEntity<?> deleteAccount(@PathVariable("token") String token) {
		ClientSession clientSession = isActive(token);
		if (clientSession != null) {
			clientSession.setLastAccessed(System.currentTimeMillis());
			try {
				return new ResponseEntity<>(clientServiceImpl.deleteAccount(), HttpStatus.OK);
			} catch (Exception e) {
				e.getMessage();
				return new ResponseEntity<>("Failed remove account.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Unauthorized. Session Timeout", HttpStatus.UNAUTHORIZED);
		}
	}

}
