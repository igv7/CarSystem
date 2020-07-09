package com.Igor.CarSystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.service.CarServiceImpl;

@RestController
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarServiceImpl carServiceImpl;

	// View All Cars
	@GetMapping("/viewAllCars")
	public ResponseEntity<?> getAllCars() {
		try {
			return new ResponseEntity<>(carServiceImpl.getAllCars(), HttpStatus.OK);
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Failed to view all cars", HttpStatus.BAD_REQUEST);
		}

	}

	// View all Cars By CarType
	@GetMapping("/viewAllCarsByCarType/{type}")
	public ResponseEntity<?> getAllCarsByCarType(@PathVariable("type") CarType type) {
		try {
			return new ResponseEntity<>(carServiceImpl.getAllCarsByType(type), HttpStatus.OK);
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Faild to get all cars by type!", HttpStatus.BAD_REQUEST);
		}

	}

}
