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
	
	/** Next receipt ID. Kept in memory only, so it restarts at 1 when the application restarts. */
	private static long id = 1;

	/**
	 * Returns the next receipt ID and advances the counter.
	 * @return the receipt ID to use for a new receipt
	 */
	public static long incrementId() {
		return id++;
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
