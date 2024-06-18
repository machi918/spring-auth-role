package com.spring.auth_test.authentication.service;

import com.spring.auth_test.authentication.model.AuthenticationRequest;
import com.spring.auth_test.authentication.model.AuthenticationResponse;
import com.spring.auth_test.authentication.model.RegisterRequest;

public interface IAuthenticationService {

    AuthenticationResponse login (AuthenticationRequest authenticationRequest);

    AuthenticationResponse register(RegisterRequest registerRequest);
}
