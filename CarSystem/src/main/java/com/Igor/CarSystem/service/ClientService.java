package com.Igor.CarSystem.service;

import java.util.List;

import com.Igor.CarSystem.model.Car;

public interface ClientService {
	
	public void setClientId(int id);
	
	public Car getCar(int id) throws Exception;
	
	public List<Car> getCars() throws Exception;
	
	public Car returnCar(int id) throws Exception;

}
