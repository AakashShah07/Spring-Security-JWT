package com.example.User_JWT.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericApiResponse<T> {

    private int status;
    private String message;
    private T Data;


}
