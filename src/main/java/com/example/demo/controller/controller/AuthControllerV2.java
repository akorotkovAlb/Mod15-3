package com.example.demo.controller.controller;

import com.example.demo.controller.config.jwt.JwtUtils;
import com.example.demo.controller.config.jwt.UserDetailsImpl;
import com.example.demo.controller.request.V2.auth.JwtResponse;
import com.example.demo.controller.request.V2.auth.LoginRequest;
import com.example.demo.controller.request.V2.auth.SignupRequest;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/V2/auth")
public class AuthControllerV2 {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserService userService;
    @Autowired private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws UserAlreadyExistException {
        userService.registerUser(signUpRequest.getUsername(), signUpRequest.getEmail(),
                signUpRequest.getPassword());
        return ResponseEntity.accepted().build();
    }
}
