package com.Igor.Tal.CarSystem.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//import javax.persistence.Id;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import com.Igor.Tal.CarSystem.enums.CarColor;
import com.Igor.Tal.CarSystem.enums.CarType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "clientReceipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ClientReceipt {
	
	private static long id = 1;

	public static long incrementId() {
		return id++;
	}
	
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
	
//	@Field("carPrice")
	private double carPrice;


}
