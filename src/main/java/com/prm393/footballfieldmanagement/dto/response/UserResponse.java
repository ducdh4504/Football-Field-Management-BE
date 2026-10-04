package com.prm393.footballfieldmanagement.dto.response;

import com.prm393.footballfieldmanagement.enums.Role;

public record UserResponse(
        Long userId,
        String fullName,
        String email,
        String phone,
        String avatarUrl,
        Role role,
        boolean isActive
) {
}
