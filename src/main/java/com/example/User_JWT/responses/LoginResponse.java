package com.example.User_JWT.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token ;
    private long expiration ;


}
