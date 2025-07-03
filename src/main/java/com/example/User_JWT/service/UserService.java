package com.example.User_JWT.service;

import com.example.User_JWT.dto.GenericApiResponse;
import com.example.User_JWT.model.User;
import com.example.User_JWT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository ;

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users ;
    }

    public GenericApiResponse<Object> updateDetails(String updateRequest, String token){
          return GenericApiResponse.builder()
                .status(200)
                .message("Token checking")
                .Data("tata")
                .build();
    }

}
