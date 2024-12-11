package com.ankit.api.gateway.controller;

import com.ankit.api.gateway.configuration.JwtHelper;
import com.ankit.api.gateway.dto.LoginRequest;
import com.ankit.api.gateway.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken
                (loginRequest.getEmail(), loginRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            String token = JwtHelper.generateToken(loginRequest.getEmail());
            return new LoginResponse(loginRequest.getEmail(), token);
        }
        else {
            throw new UsernameNotFoundException("Invalid Credentials");
        }
    }

    @GetMapping("/api/test")
    @ResponseStatus(HttpStatus.OK)
    public String testAuthApi() {
        return "Hello Auth User API";
    }

    @GetMapping("/api/free")
    @ResponseStatus(HttpStatus.OK)
    public String testFreeApi() {
        return "Hello Free Api";
    }
}
