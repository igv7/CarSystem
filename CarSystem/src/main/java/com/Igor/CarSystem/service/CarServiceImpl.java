package com.Igor.CarSystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.repo.CarRepository;

@Service
public class CarServiceImpl implements CarService, Facade {

	private static final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

	@Autowired
	private CarRepository carRepository;

	// Get all Cars
	@Override
	public List<Car> getAllCars() throws Exception {
		log.debug("************************StartGetAllCars************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAll();
				log.debug("Success on get all Cars: " + cars);
				log.debug("************************EndGetAllCars************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Failed to get all cars" + ": {}", e.getMessage());
			throw new Exception("Failed to get all cars");
		}

	}

	// Get all Cars By CarType
	@Override
	public List<Car> getAllCarsByType(CarType type) throws Exception {
		log.debug("************************StartGetAllCarsByType************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAllByType(type);
				log.debug("Success on get all Cars by type " + type + ": " + cars);
				log.debug("************************EndGetAllCarsByType************************");
				return cars;
			}
		} catch (Exception e) {
			log.error("Failed to get all cars by type " + type + ": {}", e.getMessage());
			throw new Exception("Failed to get all cars by type " + type);
		}

	}

}
