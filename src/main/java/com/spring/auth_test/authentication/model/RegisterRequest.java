package com.spring.auth_test.authentication.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements Serializable {

    @Email(message = "Wrong email format")
    private String email;

    @Length(min = 5, message = "Como mínimo {min} caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    private String role;

}
