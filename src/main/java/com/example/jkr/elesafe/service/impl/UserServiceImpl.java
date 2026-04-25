package com.example.jkr.elesafe.service.impl;

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import com.example.jkr.elesafe.model.WildOfficer;
import com.example.jkr.elesafe.repo.UserRepository;
import com.example.jkr.elesafe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
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



    // ✅ Get all Wild Officers only
    @Override
    public List<UserResponse> getAllWildOfficers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.WILD_OFFICER)
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse.UserResponseBuilder builder = UserResponse.builder()
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
                .status(user.getStatus());

        // ✅ If it's a WildOfficer, add extra fields
        if (user instanceof WildOfficer wildOfficer) {
            builder.badgeNumber(wildOfficer.getBadgeNumber());
            builder.station(wildOfficer.getStation());
        }

        return builder.build();
    }
}