package com.Igor.Tal.CarSystem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
	
	public static String getCurrentDate() {
		LocalDateTime dateTime = LocalDateTime.now();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
		
		String formattedDate = dateTime.format(dateTimeFormatter);
		return formattedDate;
	}
	
}