package com.Igor.CarSystem.exceptions;

/** Signals that a client was not found. Services catch it, but nothing currently throws it. */
public class ClientDoesntExist extends Exception {

	/** @param message description of the missing client */
	public ClientDoesntExist(String message) {
		super(message);
	}
}