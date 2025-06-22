package com.example.User_JWT.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {

    private String id;
    private String username;
    private String email;
}
