package com.yorgohaykal.album_rating_tracker.controller;

import com.yorgohaykal.album_rating_tracker.dto.AuthResponse;
import com.yorgohaykal.album_rating_tracker.dto.LoginRequest;
import com.yorgohaykal.album_rating_tracker.dto.LoginResponse;
import com.yorgohaykal.album_rating_tracker.dto.RegisterRequest;
import com.yorgohaykal.album_rating_tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
