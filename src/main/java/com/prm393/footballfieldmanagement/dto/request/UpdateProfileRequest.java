package com.prm393.footballfieldmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank(message = "Full name is required") String fullName,
        @NotBlank(message = "Phone is required") String phone,
        String avatarUrl
) {
}
