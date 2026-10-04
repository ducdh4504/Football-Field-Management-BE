package com.prm393.footballfieldmanagement.service;

import com.prm393.footballfieldmanagement.dto.request.LoginRequest;
import com.prm393.footballfieldmanagement.dto.request.RegisterRequest;
import com.prm393.footballfieldmanagement.dto.response.AuthResponse;
import com.prm393.footballfieldmanagement.dto.response.UserResponse;
import com.prm393.footballfieldmanagement.entity.User;
import com.prm393.footballfieldmanagement.enums.Role;
import com.prm393.footballfieldmanagement.exception.DuplicateEmailException;
import com.prm393.footballfieldmanagement.exception.DuplicatePhoneException;
import com.prm393.footballfieldmanagement.exception.InactiveAccountException;
import com.prm393.footballfieldmanagement.exception.InvalidCredentialsException;
import com.prm393.footballfieldmanagement.mapper.UserMapper;
import com.prm393.footballfieldmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenService jwtTokenService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtTokenService,
                new UserMapper()
        );
    }

    @Test
    void registerCreatesAnActiveCustomerWithABcryptHash() {
        RegisterRequest request = new RegisterRequest(
                "Test Customer", "CUSTOMER@TEST.COM", "0900000001", "Customer@123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(1L);
            return user;
        });

        UserResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(Role.CUSTOMER, savedUser.getRole());
        assertTrue(savedUser.isActive());
        assertEquals("customer@test.com", savedUser.getEmail());
        assertFalse("Customer@123".equals(savedUser.getPasswordHash()));
        assertTrue(passwordEncoder.matches("Customer@123", savedUser.getPasswordHash()));
        assertEquals(Role.CUSTOMER, response.role());
        assertTrue(response.isActive());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(userRepository.existsByEmailIgnoreCase("customer@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(
                new RegisterRequest("Customer", "customer@test.com", "0900000001", "password")
        ));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerRejectsDuplicatePhone() {
        when(userRepository.existsByPhone("0900000001")).thenReturn(true);

        assertThrows(DuplicatePhoneException.class, () -> authService.register(
                new RegisterRequest("Customer", "customer@test.com", "0900000001", "password")
        ));
        verify(userRepository, never()).save(any());
    }

    @Test
    void validLoginReturnsJwtAccessToken() {
        User user = activeCustomer();
        when(userRepository.findByEmailIgnoreCase("customer@test.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtTokenService.generateAccessToken(user)).thenReturn("jwt-access-token");
        when(jwtTokenService.getAccessTokenExpirationSeconds()).thenReturn(86_400L);

        AuthResponse response = authService.login(new LoginRequest("customer@test.com", "Customer@123"));

        assertEquals("jwt-access-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(86_400L, response.expiresIn());
        assertEquals("customer@test.com", response.user().email());
    }

    @Test
    void loginRejectsWrongPassword() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(activeCustomer()));
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad credentials"));

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequest("customer@test.com", "wrong-password")));
    }

    @Test
    void loginRejectsInactiveUser() {
        User inactiveUser = activeCustomer();
        inactiveUser.setActive(false);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(inactiveUser));

        assertThrows(InactiveAccountException.class,
                () -> authService.login(new LoginRequest("customer@test.com", "Customer@123")));
        verify(authenticationManager, never()).authenticate(any());
    }

    private User activeCustomer() {
        return User.builder()
                .userId(7L)
                .fullName("Test Customer")
                .email("customer@test.com")
                .phone("0900000001")
                .passwordHash(passwordEncoder.encode("Customer@123"))
                .role(Role.CUSTOMER)
                .isActive(true)
                .build();
    }
}
