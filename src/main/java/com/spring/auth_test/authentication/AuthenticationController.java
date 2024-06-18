package com.spring.auth_test.authentication;

import com.spring.auth_test.authentication.model.AuthenticationRequest;
import com.spring.auth_test.authentication.model.AuthenticationResponse;
import com.spring.auth_test.authentication.model.RegisterRequest;
import com.spring.auth_test.authentication.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse response = this.authenticationService.login(authenticationRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){
        AuthenticationResponse response = this.authenticationService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
