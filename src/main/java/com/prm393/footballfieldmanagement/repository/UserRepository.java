package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndUserIdNot(String phone, Long userId);
}
