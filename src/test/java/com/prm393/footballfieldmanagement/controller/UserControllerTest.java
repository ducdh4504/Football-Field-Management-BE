package com.prm393.footballfieldmanagement.controller;

import com.prm393.footballfieldmanagement.dto.request.UpdateProfileRequest;
import com.prm393.footballfieldmanagement.dto.response.ApiResponse;
import com.prm393.footballfieldmanagement.dto.response.UserResponse;
import com.prm393.footballfieldmanagement.enums.Role;
import com.prm393.footballfieldmanagement.service.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    void currentUserEndpointsUseTheAuthenticatedJwtSubject() {
        CurrentUserService currentUserService = mock(CurrentUserService.class);
        UserController controller = new UserController(currentUserService);
        UserResponse user = new UserResponse(7L, "Customer", "customer@test.com", "0900000001",
                null, Role.CUSTOMER, true);
        UpdateProfileRequest update = new UpdateProfileRequest("Updated Customer", "0900000002", null);
        when(currentUserService.getCurrentUser(7L)).thenReturn(user);
        when(currentUserService.updateCurrentUser(7L, update)).thenReturn(user);

        ApiResponse<UserResponse> getResponse = controller.getCurrentUser(jwtForUserId(7L));
        ApiResponse<UserResponse> updateResponse = controller.updateCurrentUser(jwtForUserId(7L), update);

        assertEquals(7L, getResponse.data().userId());
        assertEquals(7L, updateResponse.data().userId());
        verify(currentUserService).getCurrentUser(7L);
        verify(currentUserService).updateCurrentUser(7L, update);
    }

    private Jwt jwtForUserId(Long userId) {
        Instant now = Instant.now();
        return Jwt.withTokenValue("access-token")
                .header("alg", "HS256")
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60))
                .build();
    }
}
