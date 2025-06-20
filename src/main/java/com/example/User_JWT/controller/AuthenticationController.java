package com.example.User_JWT.controller;


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
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDTO registerUserDTO){
        User registerUser = authenticationService.signup(registerUserDTO);
        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        User authUSer = authenticationService.authenticate(loginUserDTO);
        String jwtToken = jwtService.generateToken(authUSer);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getJwtExpiration());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public  ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO){
        try{
            authenticationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("Account Verified Successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try{
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code send");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
