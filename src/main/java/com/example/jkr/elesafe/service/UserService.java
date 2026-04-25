package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import java.util.List;

public interface UserService {
    UserResponse getUserProfile(String email);
    User getUserByNic(String nic);
    List<UserResponse> getAllUsers();
    List<UserResponse> getAllWildOfficers();  // ✅ keep this
}