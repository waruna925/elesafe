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
public class UserResponse {

    private String userId;
    private String nic;
    private String lastName;
    private String firstName;
    private User.Role role;
    private String email;
    private String phoneNumber;
    private User.Gender gender;
    private String address;
    private String district;
    private String village;
    private User.UserStatus status;
}