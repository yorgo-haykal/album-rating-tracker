package com.yorgohaykal.album_rating_tracker.service;

import com.yorgohaykal.album_rating_tracker.dto.AuthResponse;
import com.yorgohaykal.album_rating_tracker.dto.LoginRequest;
import com.yorgohaykal.album_rating_tracker.dto.LoginResponse;
import com.yorgohaykal.album_rating_tracker.dto.RegisterRequest;
import com.yorgohaykal.album_rating_tracker.entity.AppUser;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import com.yorgohaykal.album_rating_tracker.repository.AppUserRepository;
import com.yorgohaykal.album_rating_tracker.repository.ScoringWeightsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final ScoringWeightsRepository scoringWeightsRepository;
    private final ScoringService scoringService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        AppUser savedUser = appUserRepository.save(user);

        ScoringWeights defaultWeights = scoringService.createDefaultWeights();
        defaultWeights.setUser(savedUser);
        scoringWeightsRepository.save(defaultWeights);

        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername());
        return new LoginResponse(token, user.getId(), user.getUsername());
    }
}
