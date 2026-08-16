package com.yorgohaykal.album_rating_tracker.service;

import com.yorgohaykal.album_rating_tracker.dto.AuthResponse;
import com.yorgohaykal.album_rating_tracker.dto.RegisterRequest;
import com.yorgohaykal.album_rating_tracker.entity.AppUser;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import com.yorgohaykal.album_rating_tracker.repository.AppUserRepository;
import com.yorgohaykal.album_rating_tracker.repository.ScoringWeightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ScoringWeightsRepository scoringWeightsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ScoringService scoringService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringService();
        authService = new AuthService(appUserRepository, scoringWeightsRepository, scoringService, passwordEncoder);

        validRequest = new RegisterRequest();
        validRequest.setUsername("testuser");
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("plaintextpassword");
    }

    @Test
    void register_withValidRequest_createsUserAndDefaultWeights() {
        when(appUserRepository.existsByUsername("testuser")).thenReturn(false);
        when(appUserRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plaintextpassword")).thenReturn("hashed_password_123");

        AppUser savedUser = new AppUser();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(savedUser);

        AuthResponse response = authService.register(validRequest);

        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());

        verify(passwordEncoder).encode("plaintextpassword");

        verify(scoringWeightsRepository).save(any(ScoringWeights.class));
    }

    @Test
    void register_withDuplicateUsername_throwsException() {
        when(appUserRepository.existsByUsername("testuser")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(validRequest)
        );
        assertTrue(exception.getMessage().contains("Username"));

        // Should never attempt to save if validation fails early
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void register_withDuplicateEmail_throwsException() {
        when(appUserRepository.existsByUsername("testuser")).thenReturn(false);
        when(appUserRepository.existsByEmail("test@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(validRequest)
        );
        assertTrue(exception.getMessage().contains("Email"));

        verify(appUserRepository, never()).save(any());
    }

    @Test
    void register_neverStoresPlaintextPassword() {
        when(appUserRepository.existsByUsername(any())).thenReturn(false);
        when(appUserRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("bcrypt_hashed_value");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser userArg = invocation.getArgument(0);
            // Whatever gets passed to save() must NOT be the raw password
            assertNotEquals("plaintextpassword", userArg.getPasswordHash());
            assertEquals("bcrypt_hashed_value", userArg.getPasswordHash());
            userArg.setId(1L);
            return userArg;
        });

        authService.register(validRequest);
    }
}
