package com.example.jkr.elesafe.dto;

import com.example.jkr.elesafe.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Register new user request")
public class RegisterRequest {

    @NotBlank(message = "NIC is required")
    private String nic;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotNull(message = "Role is required")
    @Schema(example = "USER")
    private User.Role role;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull(message = "Gender is required")
    @Schema(example = "MALE")
    private User.Gender gender;

    @NotBlank(message = "Password is required")
    private String password;

    private String address;
    private String village;

    // ✅ Optional - only required when role = WILD_OFFICER
    private String badgeNumber;
    private String station;
}