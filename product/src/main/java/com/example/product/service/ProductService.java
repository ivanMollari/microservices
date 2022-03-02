package com.example.product.service;

import com.example.product.model.Product;

import java.util.List;

public interface ProductService {

    void save(Product product);

    List<Product> findByIdCustomer(Long idCustomer);
}
