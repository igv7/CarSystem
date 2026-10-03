package com.Igor.CarSystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A registered client, stored in the PostgreSQL "client" table.
 * The validation annotations are checked on request bodies marked {@code @Valid} (sign-up, create and
 * update); the same rules are used by the Angular forms.
 */
@Entity
@Table(schema = "public", name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

	/** Earliest accepted date of birth. */
	private static final LocalDate MIN_BIRTHDAY = LocalDate.of(1900, 1, 1);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	/** Login name; unique. Angle brackets are rejected so a name can never carry HTML. */
	@NotBlank(message = "is required")
	@Size(min = 2, max = 30, message = "must be 2-30 characters")
	@Pattern(regexp = "[^<>]*", message = "must not contain < or >")
	@Column(name = "NAME")
	private String name;

	/** Date of birth as yyyy-MM-dd text (column DOB). */
	@NotBlank(message = "is required")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "must have the format yyyy-MM-dd")
	@Column(name = "DOB")
	private String birthday;

	/** Plain-text login password. Excluded from {@code toString()} so it never reaches the logs. */
	@ToString.Exclude // keep passwords out of log output
	@NotBlank(message = "is required")
	@Size(min = 4, max = 100, message = "must be 4-100 characters")
	@Column(name = "PASSWORD")
	private String password;

	@NotBlank(message = "is required")
	@Pattern(regexp = "\\d{3}-\\d{7}", message = "must have the format 050-1234567")
	@Column(name = "PHONE")
	private String phoneNumber;

	@NotBlank(message = "is required")
	@Size(max = 100, message = "must be at most 100 characters")
	@Pattern(regexp = "[^@\\s<>]+@[^@\\s<>]+\\.[^@\\s<>]+", message = "must be a valid email address")
	@Column(name = "EMAIL")
	private String email;

	/**
	 * Money left on the account. Renting and each billing run subtract car prices;
	 * at 0 or below the billing job returns the client's cars.
	 * Sign-up additionally limits the starting balance (see SignUpServiceImpl).
	 */
	@DecimalMin(value = "0", message = "must not be negative")
	@DecimalMax(value = "10000", message = "must be at most 10000")
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

	/**
	 * Validation rule: the birthday is a real date between 1900-01-01 and today. A badly formatted value is
	 * reported by the {@code @Pattern} on the field instead.
	 */
	@JsonIgnore
	@AssertTrue(message = "birthday must be a date between 1900-01-01 and today")
	public boolean isBirthdayInRange() {
		if (birthday == null || !birthday.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return true;
		}
		try {
			LocalDate date = LocalDate.parse(birthday);
			return !date.isBefore(MIN_BIRTHDAY) && !date.isAfter(LocalDate.now());
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	/** Stores the name without surrounding spaces. */
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/** Stores the birthday without surrounding spaces. */
	public void setBirthday(String birthday) {
		this.birthday = birthday == null ? null : birthday.trim();
	}

	/** Stores the phone number without surrounding spaces. */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
	}

	/** Stores the email without surrounding spaces. */
	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

}
