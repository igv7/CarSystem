package com.Igor.CarSystem.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.Igor.CarSystem.enums.CarColor;
import com.Igor.CarSystem.enums.CarType;

/** Checks the request validation rules on Car and Client without starting Spring or a database. */
class ModelValidationTest {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/** Names of the properties that break a rule. */
	private static <T> Set<String> invalidFields(T bean) {
		Set<ConstraintViolation<T>> violations = validator.validate(bean);
		return violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());
	}

	private static Car validCar() {
		Car car = new Car();
		car.setNumber("123-45-678");
		car.setColor(CarColor.RED);
		car.setType(CarType.AUDI);
		car.setAmount(1);
		car.setPrice(250);
		car.setImage("C:\\fakepath\\audi.jpg");
		return car;
	}

	private static Client validClient() {
		Client client = new Client();
		client.setName("Igor");
		client.setBirthday("1990-05-17");
		client.setPassword("secret");
		client.setPhoneNumber("050-1234567");
		client.setEmail("igor@example.com");
		client.setBalance(100);
		return client;
	}

	@Test
	void validCarAndClientPass() {
		assertTrue(invalidFields(validCar()).isEmpty());
		assertTrue(invalidFields(validClient()).isEmpty());
	}

	@Test
	void carRulesAreEnforced() {
		Car car = new Car();
		car.setNumber("12345678");
		car.setAmount(2);
		car.setPrice(0);
		car.setImage(" ");
		Set<String> fields = invalidFields(car);
		assertTrue(fields.containsAll(Set.of("number", "color", "type", "amount", "price", "image")), fields.toString());

		Car expensive = validCar();
		expensive.setPrice(1000.01);
		assertEquals(Set.of("price"), invalidFields(expensive));
	}

	@Test
	void clientRulesAreEnforced() {
		Client client = new Client();
		client.setName("<b>x</b>");
		client.setBirthday("17/05/1990");
		client.setPassword("abc");
		client.setPhoneNumber("0501234567");
		client.setEmail("not-an-email");
		client.setBalance(-1);
		Set<String> fields = invalidFields(client);
		assertTrue(fields.containsAll(Set.of("name", "birthday", "password", "phoneNumber", "email", "balance")), fields.toString());
	}

	@Test
	void htmlAndScriptAreRejectedInStoredTextFields() {
		String script = "<script>alert(1)</script>";

		Client client = validClient();
		client.setName(script);
		client.setEmail(script + "@example.com");
		client.setPhoneNumber(script);
		client.setBirthday(script);
		assertEquals(Set.of("name", "email", "phoneNumber", "birthday"), invalidFields(client));

		Car car = validCar();
		car.setNumber(script);
		car.setImage("<img src=x onerror=alert(1)>");
		assertEquals(Set.of("number", "image"), invalidFields(car));
	}

	@Test
	void birthdayMustBeBetween1900AndToday() {
		Client future = validClient();
		future.setBirthday(LocalDate.now().plusDays(1).toString());
		assertEquals(Set.of("birthdayInRange"), invalidFields(future));

		Client tooOld = validClient();
		tooOld.setBirthday("1899-12-31");
		assertEquals(Set.of("birthdayInRange"), invalidFields(tooOld));

		Client notADate = validClient();
		notADate.setBirthday("1990-02-30");
		assertEquals(Set.of("birthdayInRange"), invalidFields(notADate));
	}

	@Test
	void textFieldsAreTrimmedButPasswordsAreNot() {
		Client client = validClient();
		client.setName("  Igor ");
		client.setEmail(" igor@example.com ");
		client.setPassword(" secret ");
		assertEquals("Igor", client.getName());
		assertEquals("igor@example.com", client.getEmail());
		assertEquals(" secret ", client.getPassword());

		Car car = validCar();
		car.setNumber(" 123-45-678 ");
		assertEquals("123-45-678", car.getNumber());
	}
}
