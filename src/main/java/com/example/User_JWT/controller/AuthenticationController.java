package com.example.User_JWT.controller;


import com.example.User_JWT.service.AuthenticationService;
import com.example.User_JWT.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService ;
    private final AuthenticationService authenticationService ;




}
