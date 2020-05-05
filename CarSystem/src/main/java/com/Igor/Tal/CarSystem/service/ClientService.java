package com.Igor.Tal.CarSystem.service;

import com.Igor.Tal.CarSystem.model.Car;

public interface ClientService {
	
	public void setClientId(int id);
	
	public Car getCar(int id) throws Exception;

}
