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
	
	@Column(name = "DOB")
	private String birthday;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "PHONE")
	private String phoneNumber;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "BALANCE")
	private double balance;
	
//	@OneToMany
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Car> cars = new ArrayList<>();
	
	

}
