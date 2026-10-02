package com.Igor.CarSystem.service;

import java.util.List;

import com.Igor.CarSystem.model.Car;

/** Operations of one logged-in client. */
public interface ClientService {
	
	/** Binds this service instance to the logged-in client. */
	public void setClientId(int id);
	
	/** Rents a car for the client. */
	public Car getCar(int id) throws Exception;
	
	/** All cars in the system. */
	public List<Car> getCars() throws Exception;
	
	/** Returns one of the client's rented cars. */
	public Car returnCar(int id) throws Exception;

}
