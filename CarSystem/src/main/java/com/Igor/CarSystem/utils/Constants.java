package com.Igor.CarSystem.utils;

/**
 * Fixed business rules and formats shared across the application.
 * Values that may differ per environment (credentials, timeouts, schedules, URLs) are in
 * application.properties instead, under the {@code carsystem.*} keys.
 */
public final class Constants {

	private Constants() {
	}

	// Car availability: each Car row is one physical car, so Car.amount is only ever one of these two values

	/** {@code Car.amount} of a car that can be rented. */
	public static final int CAR_AVAILABLE = 1;

	/** {@code Car.amount} of a car that is currently rented. */
	public static final int CAR_RENTED = 0;

	// Balance thresholds

	/** A client whose balance is at or below this value cannot rent a car. */
	public static final double RENT_BLOCKED_AT_BALANCE = -1.0;

	/** The billing job returns all cars of a client whose balance is at or below this value. */
	public static final double BILLING_RETURN_AT_BALANCE = 0.0;

	// Date formats

	/** Date format of {@code Client.birthday}. */
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	/** Regex for {@link #DATE_PATTERN}; used to validate the format before parsing. */
	public static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";

	/** Date-time format of {@code ClientReceipt.receiptDate}. */
	public static final String RECEIPT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

	/** Time format used in log messages of the billing job. */
	public static final String LOG_TIME_PATTERN = "HH:mm:ss";

	// Validation

	/** Number of digits in {@code Client.clientID}. */
	public static final int CLIENT_ID_LENGTH = 9;

	/** {@code Client.clientID}: exactly {@link #CLIENT_ID_LENGTH} digits, nothing else. */
	public static final String CLIENT_ID_REGEX = "\\d{" + CLIENT_ID_LENGTH + "}";

	/** Text without '<' or '>', so values can't inject HTML into the frontend. */
	public static final String NO_HTML_REGEX = "[^<>]*";
}
