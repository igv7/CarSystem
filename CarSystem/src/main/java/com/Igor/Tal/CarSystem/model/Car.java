package com.Igor.Tal.CarSystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.Igor.Tal.CarSystem.enums.CarColor;
import com.Igor.Tal.CarSystem.enums.CarType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	@Column(name = "NUMBER")
	private String number;
	
	@Column(name = "COLOR")
	@Enumerated(EnumType.STRING)
	private CarColor color;
	
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private CarType type;
	
	@Column(name = "AMOUNT")
	private int amount;
	
	@Column(name = "PRICE")
	private double price;
	
	@Column(name = "IMAGE")
	private String image;

}
