package com.Igor.CarSystem.service;

import com.Igor.CarSystem.model.Car;

public interface ClientService {
	
	public void setClientId(int id);
	
	public Car getCar(int id) throws Exception;

}
