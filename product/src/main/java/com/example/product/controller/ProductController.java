package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("id/{idCustomer}")
    public ResponseEntity<List<Product>> getCustomers(@PathVariable("idCustomer") Long id) {
        return new ResponseEntity<>(service.findByIdCustomer(id),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Product product){
        service.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
