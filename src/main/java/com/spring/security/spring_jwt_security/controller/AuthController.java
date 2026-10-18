package com.spring.security.spring_jwt_security.controller;

import com.spring.security.spring_jwt_security.entity.AuthRequest;
import com.spring.security.spring_jwt_security.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        authRequest.getUserName(), authRequest.getPassword()
                )
        );
        return jwtUtil.generateToken(authRequest.getUserName());
    }
}
