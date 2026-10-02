package com.Igor.CarSystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** A registered client, stored in the PostgreSQL "client" table. */
@Entity
@Table(schema = "public", name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NAME")
	private String name;
	
	/** Date of birth as free text (column DOB). */
	@Column(name = "DOB")
	private String birthday;
	
	/** Plain-text login password. Excluded from {@code toString()} so it never reaches the logs. */
	@ToString.Exclude // keep passwords out of log output
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "PHONE")
	private String phoneNumber;
	
	@Column(name = "EMAIL")
	private String email;
	
	/**
	 * Money left on the account. Renting and each billing run subtract car prices;
	 * at 0 or below the billing job returns the client's cars.
	 */
	@Column(name = "BALANCE")
	private double balance;
	
	/**
	 * Cars the client currently rents. Stored in the client_cars join table and loaded eagerly.
	 * Deleting the client removes only the links; the cars stay in the catalogue.
	 * The join table has a UNIQUE constraint on cars_id: a car can be rented by one client at a time,
	 * which matches {@code Car.amount} being at most 1.
	 */
//	@OneToMany
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Car> cars = new ArrayList<>();
	
	

}
