package com.prm393.footballfieldmanagement.controller;

import com.prm393.footballfieldmanagement.dto.request.UpdateProfileRequest;
import com.prm393.footballfieldmanagement.dto.response.ApiResponse;
import com.prm393.footballfieldmanagement.dto.response.UserResponse;
import com.prm393.footballfieldmanagement.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer JWT")
public class UserController {

    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.success("Current user retrieved", currentUserService.getCurrentUser(currentUserId(jwt)));
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.success(
                "Profile updated successfully",
                currentUserService.updateCurrentUser(currentUserId(jwt), request)
        );
    }

    private Long currentUserId(Jwt jwt) {
        try {
            return Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("Invalid authenticated user");
        }
    }
}
