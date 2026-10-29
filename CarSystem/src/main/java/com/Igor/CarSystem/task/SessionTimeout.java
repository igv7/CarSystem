package com.Igor.CarSystem.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Background thread that removes idle sessions. The idle limit and check interval come from
 * {@code carsystem.session.*} in application.properties.
 */
@Component
public class SessionTimeout {

	private static final Logger log = LoggerFactory.getLogger(SessionTimeout.class);

	@Autowired
	private Map<String, ClientSession> tokensMap;

	/** Sessions idle longer than this many minutes are removed ({@code carsystem.session.timeout-minutes}). */
	@Value("${carsystem.session.timeout-minutes}")
	private long timeoutMinutes;

	/** Seconds between two cleanup runs ({@code carsystem.session.check-interval-seconds}). */
	@Value("${carsystem.session.check-interval-seconds}")
	private long checkIntervalSeconds;

	/** Set by {@link #stop()} to end the thread after its current sleep. */
	private boolean stop = false;
	
	/** Starts the cleanup thread. Called once from {@code CarSystem.init()}. */
	public void start() {
		new Thread(new Runnable() {
			
			List<String> tokensToRemove = new ArrayList<String>();
			
			@Override
			public void run() {
				while(!stop) {
					for(Map.Entry<String, ClientSession> e:tokensMap.entrySet()) {
						if (System.currentTimeMillis()-e.getValue().getLastAccessed()>TimeUnit.MINUTES.toMillis(timeoutMinutes)) {
							tokensToRemove.add(e.getKey());
						}
					}
					for(String token:tokensToRemove) {
						tokensMap.remove(token);
					}
					log.debug("Session timeout check: {} expired, {} active", tokensToRemove.size(), tokensMap.size());
					try {
						Thread.sleep(TimeUnit.SECONDS.toMillis(checkIntervalSeconds));
					} catch (Exception e) {
						log.error("Session timeout thread interrupted: {}", e.getMessage());
						throw new ClassCastException("You are not allowed to perform this action! ***" +e.getMessage());
					}
				}
				
			}
		}).start();
	}
	
	/** Asks the cleanup thread to finish. Called from {@code CarSystem.destroy()}. */
	public void stop() {
		this.stop = true;
	}

}