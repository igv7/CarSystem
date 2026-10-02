package com.Igor.CarSystem.task;

import com.Igor.CarSystem.service.Facade;

/** One logged-in session, stored in the tokens map under its token. */
public class ClientSession {
	
	/** The service this session works with: {@code AdminServiceImpl} or the client's own {@code ClientServiceImpl}. */
	private Facade facade;
	/** Time of the last request (System.currentTimeMillis()); sessions idle for 30 minutes are removed. */
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
