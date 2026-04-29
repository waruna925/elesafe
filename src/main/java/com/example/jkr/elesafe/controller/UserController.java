package com.example.jkr.elesafe.controller;

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import com.example.jkr.elesafe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    /**
     * ✅ FEATURE: Get Logged-in User Profile
     * Accessible by any authenticated user to view their own data.
     */
    @Operation(summary = "Get my profile")
    @GetMapping("/getMyProfile")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        // [cite: 55, 133, 197]
        return ResponseEntity.ok(userService.getUserProfile(userDetails.getUsername()));
    }

    /**
     * ✅ FEATURE: Get All Users (Admin Only)
     * Useful for high-level system overview.
     */
    @Operation(summary = "Get all users - Admin only")
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')") // [cite: 56, 121]
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        // [cite: 133, 199]
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * ✅ FEATURE: Get All Wild Officers (Admin Only)
     * Specifically lists officers for verification purposes.
     */
    @Operation(summary = "Get all Wild Officers - Admin only")
    @GetMapping("/getAllWildOfficers")
    @PreAuthorize("hasRole('ADMIN')") // [cite: 57, 121]
    public ResponseEntity<List<UserResponse>> getAllWildOfficers() {
        // [cite: 133, 200]
        return ResponseEntity.ok(userService.getAllWildOfficers());
    }

    /**
     * ✅ FEATURE: Activate/Update User Status (Admin Only)
     * Use this to move Wild Officers from 'PENDING' to 'ACTIVE'.
     */
    @Operation(summary = "Update user status - Admin only")
    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')") //
    public ResponseEntity<String> updateUserStatus(
            @PathVariable String userId,
            @RequestParam User.UserStatus status) {

        // [cite: 133, 136, 204]
        userService.updateUserStatus(userId, status);
        return ResponseEntity.ok("User " + userId + " status updated to " + status);
    }
}