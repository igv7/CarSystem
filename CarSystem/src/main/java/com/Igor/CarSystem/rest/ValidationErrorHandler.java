package com.Igor.CarSystem.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Turns invalid request bodies into 400 responses the frontend can show:
 * {@code {"message": "Validation failed", "errors": {"field": "problem", ...}}}.
 */
@RestControllerAdvice
public class ValidationErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ValidationErrorHandler.class);

	/** A body marked {@code @Valid} broke one or more field rules; lists every broken rule by field. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidBody(MethodArgumentNotValidException e) {
		Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			// Rules on getter methods (e.g. isBirthdayInRange) are reported under the field they check.
			String field = error.getField().equals("birthdayInRange") ? "birthday" : error.getField();
			errors.merge(field, error.getDefaultMessage(), (a, b) -> a + "; " + b);
		}
		log.info("Rejected invalid {}: {}", e.getParameter().getParameterType().getSimpleName(), errors);
		return body("Validation failed", errors);
	}

	/** The body is not valid JSON or has a value of the wrong type, such as an unknown car color. */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleUnreadableBody(HttpMessageNotReadableException e) {
		log.info("Rejected unreadable request body: {}", e.getMostSpecificCause().getMessage());
		return body("Invalid request: a value has the wrong type or is not one of the allowed values", null);
	}

	private ResponseEntity<Map<String, Object>> body(String message, Map<String, String> errors) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("message", message);
		if (errors != null) {
			body.put("errors", errors);
		}
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
