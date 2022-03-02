package com.example.customer.service;

import com.example.customer.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer findById(Long id);

    List<Customer> findByName(String name);

    void save(Customer customer1);

    void update(Long id, Customer customerUpdate);

    void delete(Long id);

    List<?> getProductsForCustomerId(Long id);

}
