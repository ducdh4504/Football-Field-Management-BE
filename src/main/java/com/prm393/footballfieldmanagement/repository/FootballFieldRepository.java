package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.FootballField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootballFieldRepository extends JpaRepository<FootballField, Long> {
}
