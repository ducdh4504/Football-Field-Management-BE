package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
