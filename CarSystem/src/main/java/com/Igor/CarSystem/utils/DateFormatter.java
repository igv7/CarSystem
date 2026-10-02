package com.Igor.CarSystem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Date formatting helpers. */
public class DateFormatter {
	
	/** @return the current local date and time as "yyyy-MM-dd HH:mm" */
	public static String getCurrentDate() {
		LocalDateTime dateTime = LocalDateTime.now();
		
//		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		String formattedDate = dateTime.format(dateTimeFormatter);
		return formattedDate;
	}
	
}