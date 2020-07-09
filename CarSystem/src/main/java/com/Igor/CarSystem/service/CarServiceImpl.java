package com.Igor.CarSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.repo.CarRepository;

@Service
public class CarServiceImpl implements CarService, Facade {

	@Autowired
	private CarRepository carRepository;

	// Get all Cars
	@Override
	public List<Car> getAllCars() throws Exception {
		System.out.println("************************StartGetAllCars************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAll();
				System.out.println("Success on get all Cars: " + cars);
				System.out.println("************************EndGetAllCars************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all cars");
		}

	}

	// Get all Cars By CarType
	@Override
	public List<Car> getAllCarsByType(CarType type) throws Exception {
		System.out.println("************************StartGetAllCarsByType************************");
		List<Car> cars = null;
		try {
			if (carRepository.findAll().isEmpty()) {
				throw new Exception("Cannot get all cars. The list is empty!");
			} else {
				cars = carRepository.findAllByType(type);
				System.out.println("Success on get all Cars by type " + type + ": " + cars);
				System.out.println("************************EndGetAllCarsByType************************");
				return cars;
			}
		} catch (Exception e) {
			throw new Exception("Failed to get all cars by type " + type);
		}

	}

}
