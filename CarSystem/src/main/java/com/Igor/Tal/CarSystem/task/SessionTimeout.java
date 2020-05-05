package com.Igor.Tal.CarSystem.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeout {
	
	@Autowired
	private Map<String, ClientSession> tokensMap;
	
	private boolean stop = false;
	
	public void start() {
		new Thread(new Runnable() {
			
			List<String> tokensToRemove = new ArrayList<String>();
			
			@Override
			public void run() {
				while(!stop) {
					for(Map.Entry<String, ClientSession> e:tokensMap.entrySet()) {
						if (System.currentTimeMillis()-e.getValue().getLastAccessed()>1000*60*30) {
							tokensToRemove.add(e.getKey());
						}
					}
					for(String token:tokensToRemove) {
						tokensMap.remove(token);
					}
					try {
						Thread.sleep(1000*60);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
			}
		}).start();
	}
	
	public void stop() {
		this.stop = true;
	}

}