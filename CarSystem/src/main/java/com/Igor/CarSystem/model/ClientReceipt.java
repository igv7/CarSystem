package com.Igor.CarSystem.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//import javax.persistence.Id;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Receipt written to MongoDB (collection "clientReceipt") each time a client rents a car.
 * Holds a copy of the client and car details at the time of rental.
 */
@Document(collection = "clientReceipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ClientReceipt {
	
	/**
	 * Next receipt ID. Kept in memory; at startup ClientReceiptServiceImpl moves it past the highest ID already
	 * in MongoDB (see {@link #continueAfter(long)}), so new receipts never overwrite stored ones.
	 */
	private static long id = 1;

	/**
	 * Returns the next receipt ID and advances the counter.
	 * @return the receipt ID to use for a new receipt
	 */
	public static synchronized long incrementId() {
		return id++;
	}

	/**
	 * Makes the next receipt ID {@code lastId + 1}, unless the counter is already past it.
	 * @param lastId the highest receipt ID already stored
	 */
	public static synchronized void continueAfter(long lastId) {
		id = Math.max(id, lastId + 1);
	}
	
	/** Mongo document ID; set from {@link #incrementId()} when the receipt is created. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Field("receiptID")
	private long receiptId = id;
	
//	@Field("clientID")
	private int clientId;
	
//	@Field("clientName")
	private String clientName;

	/**
	 * Copy of {@code Client.clientID}, the client's 9-digit personal identity number. Not to be confused with
	 * {@link #clientId}, the client's database ID. Empty on receipts saved before this field existed.
	 */
	private String clientIdentityNumber;
	
//	@Field("clientPhoneNumber")
	private String clientPhoneNumber;
	
//	@Field("clientEmail")
	private String clientEmail;
	
	/** Client's balance right after paying for this rental. */
//	@Field("clientBalance")
	private double clientBalance;
	
	/** Rental time, formatted "yyyy-MM-dd HH:mm". */
//	@Field("receiptDate")
	private String receiptDate;
	
//	@Field("carID")
	private int carId;
	
//	@Field("carNumber")
	private String carNumber;
	
//	@Field("carColor")
	@Enumerated(EnumType.STRING)
	private CarColor carColor;
	
//	@Field("carType")
	@Enumerated(EnumType.STRING)
	private CarType carType;
	
	/** Price the client paid for the car. */
//	@Field("carPrice")
	private double carPrice;
	


}
