package com.example.jkr.elesafe.dto;


import com.example.jkr.elesafe.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;
    private User.UserStatus status;
}