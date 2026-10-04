package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
