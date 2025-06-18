package com.example.User_JWT.service;

import com.example.User_JWT.dto.LoginUserDTO;
import com.example.User_JWT.dto.RegisterUserDTO;
import com.example.User_JWT.dto.VerifyUserDTO;
import com.example.User_JWT.model.User;
import com.example.User_JWT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;


    public User signup(RegisterUserDTO input)
    {
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());

        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationCode(user);
        return userRepository.save(user);

    }

    public User authenticate(LoginUserDTO input){
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!user.isEnabled()){
            throw new RuntimeException("Account not verified, Please verify you account");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return user ;

    }


    public void verifyUser(VerifyUserDTO input){

        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code has expired");
            }

            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            }  else {
                throw  new RuntimeException("Invalid verification code");
            }
        }  else {
            throw  new RuntimeException("User not found");
        }

    }

    public  void resendVerificationCode(String email){

        Optional<User> optionalUser = userRepository.findByEmail(email){
            if (optionalUser.isPresent()){
                User user = optionalUser.get();
                if(user.isEnabled()){
                    throw new RuntimeException("Account is already verified");
                }
                user.setVerificationCode(generateVerificationCode());
                user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
                sendVerificationEmail(user);
                userRepository.save(user);
            }  else {
                throw new RuntimeException("User not found in repo");
            }
        }


    }

}
