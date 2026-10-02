package com.Igor.CarSystem.service;

import java.util.List;

import com.Igor.CarSystem.enums.CarType;
import com.Igor.CarSystem.model.Car;

/** Public, read-only car catalogue. */
public interface CarService {
	
	/** All cars; fails if there are none. */
	public List<Car> getAllCars() throws Exception;
	
	/** All cars of one make; fails if there are no cars at all. */
	public List<Car> getAllCarsByType(CarType type) throws Exception;

}
