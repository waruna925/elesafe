package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.AuthResponse;
import com.example.jkr.elesafe.dto.LoginRequest;
import com.example.jkr.elesafe.dto.RegisterRequest;
import com.example.jkr.elesafe.dto.UserResponse;
import com.example.jkr.elesafe.model.User;
import com.example.jkr.elesafe.model.WildOfficer;
import com.example.jkr.elesafe.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MongoTemplate mongoTemplate; // ✅ inject MongoTemplate directly

    // ✅ Generate U0001, U0002 ... persisted in MongoDB counters collection
    private String generateUserId() {
        Query query = new Query(Criteria.where("_id").is("userId"));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);
        Map result = mongoTemplate.findAndModify(query, update, options, Map.class, "counters");
        long seq = result != null ? ((Number) result.get("seq")).longValue() : 1;
        return String.format("U%04d", seq);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }
        if (userRepository.existsByNic(request.getNic())) {
            throw new RuntimeException("NIC already registered: " + request.getNic());
        }

        User user;

        if (request.getRole() == User.Role.WILD_OFFICER) {
            if (request.getBadgeNumber() == null || request.getBadgeNumber().isBlank()) {
                throw new RuntimeException("Badge number is required for Wild Officers");
            }
            if (request.getStation() == null || request.getStation().isBlank()) {
                throw new RuntimeException("Station is required for Wild Officers");
            }

            user = WildOfficer.builder()
                    .userId(generateUserId())               // ✅ U0001, U0002...
                    .nic(request.getNic())
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .role(User.Role.WILD_OFFICER)
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(request.getAddress())
                    .village(request.getVillage())
                    .status(User.UserStatus.PENDING)
                    .badgeNumber(request.getBadgeNumber())
                    .station(request.getStation())
                    .build();

        } else {
            if (request.getBadgeNumber() != null && !request.getBadgeNumber().isBlank()) {
                throw new RuntimeException("Badge number is only allowed for Wild Officers");
            }
            if (request.getStation() != null && !request.getStation().isBlank()) {
                throw new RuntimeException("Station is only allowed for Wild Officers");
            }

            user = User.builder()
                    .userId(generateUserId())               // ✅ U0003, U0004...
                    .nic(request.getNic())
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .role(request.getRole() != null ? request.getRole() : User.Role.USER)
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(request.getAddress())
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
                .village(user.getVillage())
                .status(user.getStatus())
                .build();
    }
}