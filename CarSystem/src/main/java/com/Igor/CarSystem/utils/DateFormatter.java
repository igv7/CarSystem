package com.Igor.CarSystem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Date formatting helpers. */
public class DateFormatter {
	
	/** @return the current local date and time in {@link Constants#RECEIPT_DATE_TIME_PATTERN} format */
	public static String getCurrentDate() {
		LocalDateTime dateTime = LocalDateTime.now();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.RECEIPT_DATE_TIME_PATTERN);
		
		String formattedDate = dateTime.format(dateTimeFormatter);
		return formattedDate;
	}
	
}