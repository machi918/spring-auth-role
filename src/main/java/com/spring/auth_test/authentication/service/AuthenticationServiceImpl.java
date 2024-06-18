package com.spring.auth_test.authentication.service;

import com.spring.auth_test.authentication.config.JWTService;
import com.spring.auth_test.authentication.model.AuthenticationRequest;
import com.spring.auth_test.authentication.model.AuthenticationResponse;
import com.spring.auth_test.authentication.model.RegisterRequest;
import com.spring.auth_test.authentication.model.RolesEnum;
import com.spring.auth_test.exceptions.BadCredentialsException;
import com.spring.auth_test.exceptions.NotFoundException;
import com.spring.auth_test.models.User;
import com.spring.auth_test.repositories.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse login (AuthenticationRequest authenticationRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );

            User user = this.usersRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

            String token = this.jwtService.generateToken(user);

            System.out.println("TOKEN: " + token);
            return AuthenticationResponse.builder()
                    .token(token)
                    .build();

        } catch (DisabledException e) {
            throw new BadCredentialsException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS");
        }
    }

    public AuthenticationResponse register(RegisterRequest registerRequest){
        RolesEnum role = EnumUtils.findEnumInsensitiveCase(RolesEnum.class,registerRequest.getRole());

        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();

        usersRepository.save(newUser);
        String token =  this.jwtService.generateToken(newUser);
        System.out.println("TOKEN: " + token);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

}
