package com.example.User_JWT.controller;


import com.example.User_JWT.dto.GenericApiResponse;
import com.example.User_JWT.dto.LoginUserDTO;
import com.example.User_JWT.dto.RegisterUserDTO;
import com.example.User_JWT.dto.VerifyUserDTO;
import com.example.User_JWT.model.User;
import com.example.User_JWT.responses.LoginResponse;
import com.example.User_JWT.service.AuthenticationService;
import com.example.User_JWT.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService ;
    private final AuthenticationService authenticationService ;

    @PostMapping("/signup")
    public ResponseEntity<GenericApiResponse<Object>> registerUser(@RequestBody RegisterUserDTO registerUserDTO){
        return ResponseEntity.ok(authenticationService.signup(registerUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<GenericApiResponse<LoginResponse>> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        User authUSer = authenticationService.authenticate(loginUserDTO);
        String jwtToken = jwtService.generateToken(authUSer);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getJwtExpiration());
        GenericApiResponse<LoginResponse> response = GenericApiResponse.<LoginResponse>builder()
                .status(200)
                .message("Login Successful")
                .Data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    //Verify using email Token
    @PostMapping("/verify")
    public  ResponseEntity<GenericApiResponse<String>> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO){
            return ResponseEntity.ok(authenticationService.verifyUser(verifyUserDTO));
    }

    @PostMapping("/resend")
    public ResponseEntity<GenericApiResponse<String>> resendVerificationCode(@RequestParam String email){
            return ResponseEntity.ok(authenticationService.resendVerificationCode(email));
    }



}
