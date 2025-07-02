package com.example.User_JWT.service;

import com.example.User_JWT.dto.*;
import com.example.User_JWT.model.Role;
import com.example.User_JWT.model.User;
import com.example.User_JWT.repository.UserRepository;
import com.example.User_JWT.responses.LoginResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtService jwtService;


    public GenericApiResponse<Object> signup(RegisterUserDTO input)
    {
        try {
            User user =  User.builder()
                    .username(input.getUsername())
                            .email(input.getEmail())
                                    .password(passwordEncoder.encode(input.getPassword()))
                                            .enabled(false)
                    .verificationCode(generateVerificationCode())
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .role(Role.USER)
                    .build();

            sendVerificationCode(user);
            User savedUser = userRepository.save(user);

            UserResponseDTO responseDTO = UserResponseDTO.builder()
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .email(savedUser.getEmail())
                    .build();

            return GenericApiResponse.builder()
                    .status(200)
                    .message("User registered successfully. Verification code sent.")
                    .Data(responseDTO)
                    .build();

        } catch (Exception exception) {

            return buildResponse("Failed", HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());

        }
    }


//    TODO: Add Captcha and rate limiting
    public GenericApiResponse<Object> authenticate(LoginUserDTO input){
        try{
            Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());


            if (optionalUser.isEmpty()) {

            Thread.sleep(1000);
            return invalidCredentialsResponse();
            }

            User user = optionalUser.get();

            if (!user.isEnabled()) {
                return GenericApiResponse.<Object>builder()
                        .status(401)
                        .message("Account not verified. Please verify your account.")
                        .build();
            }

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                input.getEmail(),
                                input.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                return invalidCredentialsResponse();
            }
            String token = jwtService.generateToken(user);
            LoginResponse response = new LoginResponse(token, jwtService.getJwtExpiration(), user);

            return GenericApiResponse.<Object>builder()
                    .status(200)
                    .message("Login Successful")
                    .Data(response)
                    .build();
        }
        catch (Exception e){
            return GenericApiResponse.<Object>builder()
                    .status(500)
                    .message("Login failed: ")
                    .Data(e.getMessage())
                    .build();
        }

    }

    private GenericApiResponse<Object> invalidCredentialsResponse() {
        return GenericApiResponse.<Object>builder()
                .status(401)
                .message("Invalid email or password.")
                .Data(Collections.emptyMap())
                .build();
    }


    public GenericApiResponse<String> verifyUser(VerifyUserDTO input){

        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                return GenericApiResponse.<String>builder()
                        .status(400)
                        .message("Verification code has expired")
                        .Data(null)
                        .build();
            }

            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);

                return GenericApiResponse.<String>builder()
                        .status(200)
                        .message("User verified successfully")
                        .Data("Verified")
                        .build();
            }  else {
                return GenericApiResponse.<String>builder()
                        .status(400)
                        .message("Invalid verification code")
                        .Data(null)
                        .build();
            }
        }  else {
            return GenericApiResponse.<String>builder()
                    .status(404)
                    .message("User not found")
                    .Data(null)
                    .build();
        }

    }

    public  GenericApiResponse<String> resendVerificationCode(String email){

        Optional<User> optionalUser = userRepository.findByEmail(email) ;
        if (optionalUser.isPresent()){
                User user = optionalUser.get();
                if(user.isEnabled()){
                    return GenericApiResponse.<String>builder()
                            .status(400)
                            .message("Account is already verified")
                            .Data(null)
                            .build();
                }
                user.setVerificationCode(generateVerificationCode());
                user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
                sendVerificationCode(user);
                userRepository.save(user);
            return GenericApiResponse.<String>builder()
                    .status(200)
                    .message("Verification code sent successfully")
                    .Data("Code Sent")
                    .build();
            }  else {
            return GenericApiResponse.<String>builder()
                    .status(404)
                    .message("User not found")
                    .Data(null)
                    .build();
        }


    }

    public void sendVerificationCode(User user){

        String subject = "Account verification" ;
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try{
            emailService.sendVerificationEmail(user.getEmail(),subject, htmlMessage );

        }catch (MessagingException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return  String.valueOf(code);
    }

    private GenericApiResponse<Object> buildResponse(String message, int status, Object data) {

        return GenericApiResponse.builder()
                .message(message)
                .Data(data)
                .status(status)
                .build();
    }

}
