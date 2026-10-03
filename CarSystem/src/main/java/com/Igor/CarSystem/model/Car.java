package com.Igor.CarSystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A rentable car model, stored in the PostgreSQL "car" table.
 * The validation annotations are checked on request bodies marked {@code @Valid} (create and update);
 * the same rules are used by the Angular forms.
 */
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
	@NotBlank(message = "is required")
	@Pattern(regexp = "\\d{3}-\\d{2}-\\d{3}", message = "must have the format 123-45-678")
	@Column(name = "NUMBER")
	private String number;

	@NotNull(message = "is required")
	@Column(name = "COLOR")
	@Enumerated(EnumType.STRING)
	private CarColor color;

	@NotNull(message = "is required")
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private CarType type;

	/**
	 * Availability of this single car: 1 = available, 0 = rented. createCar always sets 1,
	 * renting sets 0, every return sets 1, and updateCar rejects any other value.
	 */
	@Min(value = 0, message = "must be 0 (rented) or 1 (available)")
	@Max(value = 1, message = "must be 0 (rented) or 1 (available)")
	@Column(name = "AMOUNT")
	private int amount;

	/** Amount charged to the client when renting, and again on every billing run while rented. */
	@DecimalMin(value = "0", inclusive = false, message = "must be greater than 0")
	@DecimalMax(value = "1000", message = "must be at most 1000")
	@Column(name = "PRICE")
	private double price;

	/** Image URL or path shown by the frontend. */
	@NotBlank(message = "is required")
	@Size(max = 255, message = "must be at most 255 characters")
	@Pattern(regexp = "[^<>]*", message = "must not contain < or >")
	@Column(name = "IMAGE")
	private String image;

	/** Stores the number without surrounding spaces. */
	public void setNumber(String number) {
		this.number = number == null ? null : number.trim();
	}

	/** Stores the image path without surrounding spaces. */
	public void setImage(String image) {
		this.image = image == null ? null : image.trim();
	}

}
