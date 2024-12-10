package com.ankit.api.gateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email(message = "Email is in Invalid Format")
    private String email;

    @NotBlank
    @Size(min = 6, max = 20, message = "Password should between 6 and 20 characters")
    private String password;

}
