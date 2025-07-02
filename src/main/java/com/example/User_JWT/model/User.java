package com.example.User_JWT.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User implements UserDetails {

    @Id
    private String id; // MongoDB uses String (ObjectId), not Long

    private String username;
    private String email;
    private String password;
    private boolean enabled;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String prakritiType;
    private int oxygenPoints;

    private Role role;

    private String verificationCode;
    private LocalDateTime verificationCodeExpiresAt;

    public User(String username, String email, String encode) {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // or roles if you use them
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
}
