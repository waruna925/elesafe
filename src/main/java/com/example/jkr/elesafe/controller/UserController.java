package com.example.jkr.elesafe.controller;

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    // Injecting the Interface ensures loose coupling
    private final UserService userService;

    /**
     * Endpoint: GET /api/users/me
     * Purpose: Returns the profile of the currently logged-in user.
     * Security: Requires a valid JWT Bearer token.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername() automatically contains the email extracted from the JWT token
        return ResponseEntity.ok(userService.getUserProfile(userDetails.getUsername()));
    }

    /**
     * Endpoint: GET /api/users/all
     * Purpose: Returns a list of all registered users in the database.
     * Security: Currently accessible to any logged-in user (We can restrict this to ADMINs later).
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}