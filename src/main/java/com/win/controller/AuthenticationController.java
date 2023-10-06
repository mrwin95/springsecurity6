package com.win.controller;

import com.win.dto.LoginDto;
import com.win.dto.LoginResponseDto;
import com.win.dto.RegistrationDto;
import com.win.models.ApplicationUser;
import com.win.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDto registrationDto){
        return authenticationService.registerUser(registrationDto.getUsername(), registrationDto.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginDto loginDto){
        if(Objects.isNull(loginDto.getUsername()) || Objects.isNull(loginDto.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LoginResponseDto.builder().message("Invalid username or password").build());
        }
        return ResponseEntity.ok(authenticationService.loginUser(loginDto.getUsername(), loginDto.getPassword()));
    }
}
