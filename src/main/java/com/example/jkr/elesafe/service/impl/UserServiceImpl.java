package com.example.jkr.elesafe.service.impl; // <-- 1. Updated package path

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import com.example.jkr.elesafe.repo.UserRepository;
import com.example.jkr.elesafe.service.UserService; // <-- 2. New Import added
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToUserResponse(user);
    }

    @Override
    public User getUserByNic(String nic) {
        return userRepository.findByNic(nic)
                .orElseThrow(() -> new RuntimeException("User not found with NIC: " + nic));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .nic(user.getNic())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .address(user.getAddress())
                .district(user.getDistrict())
                .village(user.getVillage())
                .status(user.getStatus())
                .build();
    }
}