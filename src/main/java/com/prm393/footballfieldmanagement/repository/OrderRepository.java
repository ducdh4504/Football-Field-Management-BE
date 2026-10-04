package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
