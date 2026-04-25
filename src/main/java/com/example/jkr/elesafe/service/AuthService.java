package com.example.jkr.elesafe.service;



import com.example.jkr.elesafe.dto.AuthResponse;
import com.example.jkr.elesafe.dto.LoginRequest;
import com.example.jkr.elesafe.dto.RegisterRequest;
import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import com.example.jkr.elesafe.model.WildOfficer;
import com.example.jkr.elesafe.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }
        if (userRepository.existsByNic(request.getNic())) {
            throw new RuntimeException("NIC already registered: " + request.getNic());
        }

        User user;

        if (request.getRole() == User.Role.WILD_OFFICER) {
            // ✅ Wild Officer — badgeNumber and station are REQUIRED
            if (request.getBadgeNumber() == null || request.getBadgeNumber().isBlank()) {
                throw new RuntimeException("Badge number is required for Wild Officers");
            }
            if (request.getStation() == null || request.getStation().isBlank()) {
                throw new RuntimeException("Station is required for Wild Officers");
            }

            user = WildOfficer.builder()
                    .nic(request.getNic())
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .role(User.Role.WILD_OFFICER)
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(request.getAddress())
                    .district(request.getDistrict())
                    .village(request.getVillage())
                    .status(User.UserStatus.ACTIVE)
                    .badgeNumber(request.getBadgeNumber())
                    .station(request.getStation())
                    .build();

        } else {
            // ✅ Normal User — badgeNumber and station are NOT ALLOWED
            if (request.getBadgeNumber() != null && !request.getBadgeNumber().isBlank()) {
                throw new RuntimeException("Badge number is only allowed for Wild Officers");
            }
            if (request.getStation() != null && !request.getStation().isBlank()) {
                throw new RuntimeException("Station is only allowed for Wild Officers");
            }

            user = User.builder()
                    .nic(request.getNic())
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .role(request.getRole() != null ? request.getRole() : User.Role.USER)
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(request.getAddress())
                    .district(request.getDistrict())
                    .village(request.getVillage())
                    .status(User.UserStatus.ACTIVE)
                    .build();
        }

        userRepository.save(user);

        String accessToken  = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return buildAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken  = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return buildAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
        String newAccessToken = jwtService.generateToken(user);
        return buildAuthResponse(user, newAccessToken, refreshToken);
    }

    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
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