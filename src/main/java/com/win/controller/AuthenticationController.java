package com.win.controller;

import com.win.dto.RegistrationDto;
import com.win.models.ApplicationUser;
import com.win.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
