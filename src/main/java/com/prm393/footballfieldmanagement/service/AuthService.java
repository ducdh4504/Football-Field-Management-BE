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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        String phone = request.phone().trim();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException();
        }
        if (userRepository.existsByPhone(phone)) {
            throw new DuplicatePhoneException();
        }

        User user = User.builder()
                .fullName(request.fullName().trim())
                .email(email)
                .phone(phone)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .avatarUrl(null)
                .isActive(true)
                .build();
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);
        if (!user.isActive()) {
            throw new InactiveAccountException();
        }

        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, request.password()));
        } catch (DisabledException exception) {
            throw new InactiveAccountException();
        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException();
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }

        return new AuthResponse(
                jwtTokenService.generateAccessToken(user),
                "Bearer",
                jwtTokenService.getAccessTokenExpirationSeconds(),
                userMapper.toResponse(user)
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
