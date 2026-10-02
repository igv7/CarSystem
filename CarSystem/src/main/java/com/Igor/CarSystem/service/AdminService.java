package com.Igor.CarSystem.service;

import java.util.List;

import com.Igor.CarSystem.exceptions.CarDoesntExist;
import com.Igor.CarSystem.exceptions.ClientDoesntExist;
import com.Igor.CarSystem.model.Car;
import com.Igor.CarSystem.model.Client;


/** Admin operations on clients and cars. */
public interface AdminService {
	
	//Client Operations
	/** Creates a client; fails if the name is taken. */
	public Client createClient(Client client) throws Exception;
	
	/** Overwrites the client with the same ID. */
	public Client updateClient(Client client) throws Exception;
	
	/** Finds a client by ID. */
	public Client getClientById(int id) throws Exception;
	
	/** All clients; fails if there are none. */
	public List<Client> getAllClients() throws Exception;
	
	/** Deletes a client and marks their rented cars available again; the cars stay in the catalogue. */
	public Client deleteClient(int id) throws ClientDoesntExist, Exception;
	
	
	
	//Car Operations
	/** Creates a car; fails if the number is taken. */
	public Car createCar(Car car) throws Exception;
	
	/** Overwrites the car with the same ID. */
	public Car updateCar(Car car) throws Exception;
	
	/** Finds a car by ID. */
	public Car getCarById(int id) throws Exception;
	
	/** Finds a car by number. */
	public Car getCarByNumber(String number) throws Exception;

	/** All cars; fails if there are none. */
	public List<Car> getAllCars() throws Exception;

	/** Deletes a car, unlinking it from its renter. */
	public Car deleteCar(int id) throws CarDoesntExist, Exception;

}
