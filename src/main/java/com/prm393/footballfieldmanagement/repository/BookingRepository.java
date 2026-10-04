package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
