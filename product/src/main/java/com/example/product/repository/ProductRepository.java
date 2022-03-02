package com.example.product.repository;

import com.example.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<List<Product>> findByIdCustomer(Long idCustomer);

    Optional<Product> findByCodeAndDescription(String code, String description);
}
