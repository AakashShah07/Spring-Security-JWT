package com.example.User_JWT.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    @Value(("${security_jwt_secret_key}"))
    private String secretKey;

    @Value(("${security_jwt_expiration_time}"))
    private long jwtExpiration;

    public  String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return  generateToken((new HashMap<>(), userDetails);
    }

//    public String generateToke

}
