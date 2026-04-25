package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import java.util.List;

public interface UserService {

    // Fetch a secure DTO profile using email (for the logged-in user)
    UserResponse getUserProfile(String email);

    // Fetch the raw User entity by NIC (useful for internal backend logic)
    User getUserByNic(String nic);

    // Fetch all users (for Admin dashboard later)
    List<UserResponse> getAllUsers();
}