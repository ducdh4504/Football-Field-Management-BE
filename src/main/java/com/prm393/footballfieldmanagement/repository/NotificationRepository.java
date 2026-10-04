package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
