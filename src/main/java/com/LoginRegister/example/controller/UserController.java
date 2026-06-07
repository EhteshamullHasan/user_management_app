package com.LoginRegister.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.LoginRegister.example.dto.LoginFormDto;
import com.LoginRegister.example.dto.RegistrationFormDto;
import com.LoginRegister.example.dto.ResetPwdFormDto;
import com.LoginRegister.example.dto.UserDto;
import com.LoginRegister.example.entities.UserEntity;
import com.LoginRegister.example.service.UserService;

@RestController
public class UserController {
	
	public UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	
	@PostMapping("/saveUser")
	public ResponseEntity<UserEntity> saveUser(@RequestBody RegistrationFormDto dto){
		
		UserEntity savedUser = userService.saveUser(dto);
		
		if(null!= savedUser) {
			
			return ResponseEntity.ok(savedUser);
		}
		return null;

	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginFormDto loginDto){
		UserDto loginUser = userService.login(loginDto);
		if(null!= loginUser) {
			return ResponseEntity.ok(loginUser);
		}
		return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}
	
	@PostMapping("/resetPass")
	public Boolean resetPassword(@RequestBody ResetPwdFormDto resetDto) {

		boolean resetPwd = userService.resetPwd(resetDto);
		
		return resetPwd;
	}
	

}
