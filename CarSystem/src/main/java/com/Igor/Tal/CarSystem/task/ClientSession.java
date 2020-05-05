package com.Igor.Tal.CarSystem.task;

import com.Igor.Tal.CarSystem.service.Facade;

public class ClientSession {
	
	private Facade facade;
	private long lastAccessed;
	
	
	public Facade getFacade() {
		return facade;
	}
	public void setFacade(Facade facade) {
		this.facade = facade;
	}
	public long getLastAccessed() {
		return lastAccessed;
	}
	public void setLastAccessed(long lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

}
