package com.example.customer.controller;

import com.example.customer.model.Customer;
import com.example.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/v1/customers")
public class CustomerController {

    private final CustomerService service;

    @GetMapping("id/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<Customer>> getCustomers(@PathVariable("name") String name) {
        return new ResponseEntity<>(service.findByName(name),HttpStatus.OK);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<List<?>> getProductsByIdCustomer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getProductsForCustomerId(id),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Customer customer){
        service.save(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Customer customer, @PathVariable Long id){
        service.update(id,customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
