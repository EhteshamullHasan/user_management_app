package com.LoginRegister.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.LoginRegister.example.dto.LoginFormDto;
import com.LoginRegister.example.dto.RegistrationFormDto;
import com.LoginRegister.example.dto.ResetPwdFormDto;
import com.LoginRegister.example.dto.UserDto;
import com.LoginRegister.example.entities.UserEntity;
import com.LoginRegister.example.service.UserService;

@RestController
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/saveUser")
    public ResponseEntity<UserEntity> saveUser(
            @RequestBody RegistrationFormDto dto) {

        log.info("Registration request received for email: {}",
                dto.getEmail());

        UserEntity savedUser = userService.saveUser(dto);

        if (savedUser != null) {

            log.info("User registered successfully with id: {}",
                    savedUser.getUserId());

            return ResponseEntity.ok(savedUser);
        }

        log.warn("User registration failed for email: {}",
                dto.getEmail());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody LoginFormDto loginDto) {

        log.info("Login request received for email: {}",
                loginDto.getEmail());

        UserDto loginUser = userService.login(loginDto);

        if (loginUser != null) {

            log.info("Login successful for email: {}",
                    loginDto.getEmail());

            return ResponseEntity.ok(loginUser);
        }

        log.warn("Login failed for email: {}",
                loginDto.getEmail());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }

    @PostMapping("/resetPass")
    public Boolean resetPassword(
            @RequestBody ResetPwdFormDto resetDto) {

        log.info("Password reset request received for email: {}",
                resetDto.getEmail());

        boolean resetPwd = userService.resetPwd(resetDto);

        if (resetPwd) {
            log.info("Password reset successful for email: {}",
                    resetDto.getEmail());
        } else {
            log.warn("Password reset failed for email: {}",
                    resetDto.getEmail());
        }

        return resetPwd;
    }
}