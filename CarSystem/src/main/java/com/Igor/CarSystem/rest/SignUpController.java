package com.Igor.CarSystem.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.service.SignUpServiceImpl;



/** Public sign-up endpoint; no token needed. */
@RestController
@RequestMapping("/carSystem")
public class SignUpController {

	private static final Logger log = LoggerFactory.getLogger(SignUpController.class);
	
	@Autowired
	private SignUpServiceImpl signUpServiceImpl;
	
	
	/**
	 * POST /carSystem/signUp. Registers a new client from the JSON body.
	 * @return 200 with the saved client, or 400 if the name is taken or saving fails
	 */
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@Valid @RequestBody Client client) {
		try {
			return new ResponseEntity<>(signUpServiceImpl.signUp(client), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Failed on sign up!" + ": {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
