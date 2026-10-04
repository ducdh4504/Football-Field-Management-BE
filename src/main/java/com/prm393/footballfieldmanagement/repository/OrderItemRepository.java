package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
