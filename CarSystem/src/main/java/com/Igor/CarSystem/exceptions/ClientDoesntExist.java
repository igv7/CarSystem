package com.Igor.CarSystem.exceptions;

/**
 * Signals that a client was not found. Thrown by the admin client-cars and receipts-by-client lookups,
 * which AdminController turns into a 404.
 */
public class ClientDoesntExist extends Exception {

	/** @param message description of the missing client */
	public ClientDoesntExist(String message) {
		super(message);
	}
}