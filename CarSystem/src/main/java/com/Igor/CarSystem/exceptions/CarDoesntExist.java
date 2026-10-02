package com.Igor.CarSystem.exceptions;

/** Signals that a car was not found. Services catch it, but nothing currently throws it. */
public class CarDoesntExist extends Exception {

	/** @param message description of the missing car */
	public CarDoesntExist(String message) {
		super(message);
	}
}