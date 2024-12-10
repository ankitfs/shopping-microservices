package com.ankit.api.gateway.controller;

import com.ankit.api.gateway.configuration.JwtHelper;
import com.ankit.api.gateway.dto.LoginRequest;
import com.ankit.api.gateway.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequest.getEmail(), loginRequest.getPassword()));
        String token = JwtHelper.generateToken(loginRequest.getEmail());
        return new LoginResponse(loginRequest.getEmail(), token);
    }

    @GetMapping("/api/auth/test")
    @ResponseStatus(HttpStatus.OK)
    public String testApi() {
        return "Hello User API";
    }
}
