package com.prm393.footballfieldmanagement.service;

import com.prm393.footballfieldmanagement.dto.request.UpdateProfileRequest;
import com.prm393.footballfieldmanagement.dto.response.UserResponse;
import com.prm393.footballfieldmanagement.entity.User;
import com.prm393.footballfieldmanagement.enums.Role;
import com.prm393.footballfieldmanagement.exception.DuplicatePhoneException;
import com.prm393.footballfieldmanagement.mapper.UserMapper;
import com.prm393.footballfieldmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    private CurrentUserService currentUserService;

    @BeforeEach
    void setUp() {
        currentUserService = new CurrentUserService(userRepository, new UserMapper());
    }

    @Test
    void getCurrentUserMapsOnlySafeFields() {
        when(userRepository.findById(7L)).thenReturn(Optional.of(existingUser()));

        UserResponse response = currentUserService.getCurrentUser(7L);

        assertEquals(7L, response.userId());
        assertEquals("customer@test.com", response.email());
        assertFalse(Arrays.stream(UserResponse.class.getRecordComponents())
                .anyMatch(component -> component.getName().equals("passwordHash")));
    }

    @Test
    void updateChangesOnlyAllowedProfileFields() {
        User user = existingUser();
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = currentUserService.updateCurrentUser(7L,
                new UpdateProfileRequest("Updated Customer", "0900000002", "https://example.com/avatar.png"));

        assertEquals("Updated Customer", response.fullName());
        assertEquals("0900000002", response.phone());
        assertEquals("https://example.com/avatar.png", response.avatarUrl());
        assertEquals("customer@test.com", user.getEmail());
        assertEquals(Role.CUSTOMER, user.getRole());
        assertEquals(true, user.isActive());
        assertEquals("bcrypt-hash", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    @Test
    void updateRejectsAPhoneOwnedByAnotherUser() {
        when(userRepository.findById(7L)).thenReturn(Optional.of(existingUser()));
        when(userRepository.existsByPhoneAndUserIdNot("0900000002", 7L)).thenReturn(true);

        assertThrows(DuplicatePhoneException.class, () -> currentUserService.updateCurrentUser(7L,
                new UpdateProfileRequest("Updated Customer", "0900000002", null)));
    }

    private User existingUser() {
        return User.builder()
                .userId(7L)
                .fullName("Test Customer")
                .email("customer@test.com")
                .phone("0900000001")
                .passwordHash("bcrypt-hash")
                .role(Role.CUSTOMER)
                .avatarUrl(null)
                .isActive(true)
                .build();
    }
}
