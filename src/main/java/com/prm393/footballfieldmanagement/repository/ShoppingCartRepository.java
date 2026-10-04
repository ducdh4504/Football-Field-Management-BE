package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
