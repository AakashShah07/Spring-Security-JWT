package com.example.User_JWT.controller;

import com.example.User_JWT.dto.GenericApiResponse;
import com.example.User_JWT.model.User;
import com.example.User_JWT.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;


@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    @Autowired
    UserService userService ;

    @PostMapping("/updateDetails")
    public ResponseEntity<GenericApiResponse<Object>> updateByUserName(@RequestBody String updateRequest,@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userService.updateDetails(updateRequest, token));
    }

    @PostMapping("/All")
    public ResponseEntity<List<User>> allUser(){
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }


}
