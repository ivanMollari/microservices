package com.example.customer.repository;

import com.example.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<List<Customer>> findByName(String name);

    Optional<Customer> findByNameAndPhone(String name, String phone);

    Optional<Customer> findByIdNotAndNameAndPhone(Long id, String name, String phone);
}
