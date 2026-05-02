package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.UpdateProfileRequest;
import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import java.util.List;

public interface UserService {
    UserResponse getUserProfile(String email);
    User getUserByNic(String nic);
    List<UserResponse> getAllUsers();
    List<UserResponse> getAllWildOfficers();

    // ✅ This must be here to match your Impl
    void updateUserStatus(String userId, User.UserStatus status);
    UserResponse updateMyProfile(String email, UpdateProfileRequest request);
}