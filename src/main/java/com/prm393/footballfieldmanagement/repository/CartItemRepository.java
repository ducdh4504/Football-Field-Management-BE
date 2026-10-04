package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
