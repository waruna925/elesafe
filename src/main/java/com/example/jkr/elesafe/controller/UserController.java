package com.example.jkr.elesafe.controller;

import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // ✅ Any logged-in user - own profile only
    @Operation(summary = "Get my profile")
    @GetMapping("/getMyProfile")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserProfile(userDetails.getUsername()));
    }

    // ✅ ADMIN only - get all users
    @Operation(summary = "Get all users - Admin only")
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }



    // ✅ ADMIN only - get all wild officers
    @Operation(summary = "Get all Wild Officers - Admin only")
    @GetMapping("/getAllWildOfficers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllWildOfficers() {
        return ResponseEntity.ok(userService.getAllWildOfficers());
    }
}