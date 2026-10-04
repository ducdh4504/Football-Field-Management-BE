package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
