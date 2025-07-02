package com.example.User_JWT.responses;

import com.example.User_JWT.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token ;
    private long expiration ;
    private User user;


}
