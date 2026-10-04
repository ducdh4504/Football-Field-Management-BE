package com.prm393.footballfieldmanagement.service;

import com.prm393.footballfieldmanagement.dto.request.UpdateProfileRequest;
import com.prm393.footballfieldmanagement.dto.response.UserResponse;
import com.prm393.footballfieldmanagement.entity.User;
import com.prm393.footballfieldmanagement.exception.CurrentUserNotFoundException;
import com.prm393.footballfieldmanagement.exception.DuplicatePhoneException;
import com.prm393.footballfieldmanagement.mapper.UserMapper;
import com.prm393.footballfieldmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(Long userId) {
        return userMapper.toResponse(findCurrentUser(userId));
    }

    @Transactional
    public UserResponse updateCurrentUser(Long userId, UpdateProfileRequest request) {
        User user = findCurrentUser(userId);
        String phone = request.phone().trim();
        if (userRepository.existsByPhoneAndUserIdNot(phone, userId)) {
            throw new DuplicatePhoneException();
        }

        user.setFullName(request.fullName().trim());
        user.setPhone(phone);
        user.setAvatarUrl(request.avatarUrl());
        return userMapper.toResponse(userRepository.save(user));
    }

    private User findCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(CurrentUserNotFoundException::new);
    }
}
