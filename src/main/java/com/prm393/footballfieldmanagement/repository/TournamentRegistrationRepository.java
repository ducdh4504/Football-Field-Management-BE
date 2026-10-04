package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.TournamentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistration, Long> {
}
