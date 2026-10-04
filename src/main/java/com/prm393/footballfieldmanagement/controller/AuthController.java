package com.prm393.footballfieldmanagement.controller;

import com.prm393.footballfieldmanagement.dto.request.LoginRequest;
import com.prm393.footballfieldmanagement.dto.request.RegisterRequest;
import com.prm393.footballfieldmanagement.dto.response.ApiResponse;
import com.prm393.footballfieldmanagement.dto.response.AuthResponse;
import com.prm393.footballfieldmanagement.dto.response.UserResponse;
import com.prm393.footballfieldmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", user));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("Login successful", authService.login(request));
    }
}
