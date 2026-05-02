package com.example.jkr.elesafe.service.impl;

import com.example.jkr.elesafe.dto.UpdateProfileRequest;
import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import com.example.jkr.elesafe.model.WildOfficer;
import com.example.jkr.elesafe.repo.UserRepository;
import com.example.jkr.elesafe.service.UserService;
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

    @Override
    public List<UserResponse> getAllWildOfficers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.WILD_OFFICER)
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserStatus(String userId, User.UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.setStatus(status);
        userRepository.save(user);
    }

    // ✅ NEW — update profile fields including profilePicture
    @Override
    public UserResponse updateMyProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        if (request.getFirstName() != null && !request.getFirstName().isBlank())
            user.setFirstName(request.getFirstName());

        if (request.getLastName() != null && !request.getLastName().isBlank())
            user.setLastName(request.getLastName());

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank())
            user.setPhoneNumber(request.getPhoneNumber());

        if (request.getVillage() != null && !request.getVillage().isBlank())
            user.setVillage(request.getVillage());

        if (request.getAddress() != null && !request.getAddress().isBlank())
            user.setAddress(request.getAddress());

        // ✅ Save the Supabase public URL into MongoDB
        if (request.getProfilePicture() != null && !request.getProfilePicture().isBlank())
            user.setProfilePicture(request.getProfilePicture());

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    // ✅ profilePicture added here so it's returned in every API response
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
                .village(user.getVillage())
                .status(user.getStatus())
                .profilePicture(user.getProfilePicture());  // ✅ THIS is the new line

        if (user instanceof WildOfficer wildOfficer) {
            builder.badgeNumber(wildOfficer.getBadgeNumber());
            builder.station(wildOfficer.getStation());
        }

        return builder.build();
    }
}