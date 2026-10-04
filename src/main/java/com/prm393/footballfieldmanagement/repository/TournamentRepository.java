package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
