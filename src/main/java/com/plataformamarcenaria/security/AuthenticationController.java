package com.plataformamarcenaria.security;

import com.plataformamarcenaria.dto.user.UserLoginDTO;
import com.plataformamarcenaria.dto.user.UserCreateDTO;
import com.plataformamarcenaria.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDTO));
    }
}