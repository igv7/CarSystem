package com.Igor.CarSystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A rentable car model, stored in the PostgreSQL "car" table. */
@Entity
@Table(schema = "public", name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	
	/** Unique car (licence) number; used to look cars up and to prevent duplicates. */
	@Column(name = "NUMBER")
	private String number;
	
	@Column(name = "COLOR")
	@Enumerated(EnumType.STRING)
	private CarColor color;
	
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private CarType type;
	
	/**
	 * Availability of this single car: 1 = available, 0 = rented. createCar always sets 1,
	 * renting sets 0, every return sets 1, and updateCar rejects any other value.
	 */
	@Column(name = "AMOUNT")
	private int amount;
	
	/** Amount charged to the client when renting, and again on every billing run while rented. */
	@Column(name = "PRICE")
	private double price;
	
	/** Image URL or path shown by the frontend. */
	@Column(name = "IMAGE")
	private String image;
	
	

}
