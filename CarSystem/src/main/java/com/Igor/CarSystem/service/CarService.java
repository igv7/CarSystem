package com.Igor.CarSystem.service;

import java.util.List;

import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;

public interface CarService {
	
	public List<Car> getAllCars() throws Exception;
	
	public List<Car> getAllCarsByType(CarType type) throws Exception;

}
