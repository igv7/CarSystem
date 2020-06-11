package com.Igor.CarSystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Igor.CarSystem.model.Client;
import com.Igor.CarSystem.service.SignUpServiceImpl;



@RestController
@RequestMapping("/carSystem")
public class SignUpController {
	
	@Autowired
	private SignUpServiceImpl signUpServiceImpl;
	
	
	//Sign Up
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestBody Client client) {
		try {
			return new ResponseEntity<>(signUpServiceImpl.signUp(client), HttpStatus.OK);
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Failed on sign up!", HttpStatus.BAD_REQUEST);
		}
	}

}
