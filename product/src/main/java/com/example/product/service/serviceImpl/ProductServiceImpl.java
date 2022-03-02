package com.example.product.service.serviceImpl;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void save(Product product) {
        if(exist(product))
            throw new IllegalStateException("The product already exists");
        else
            productRepository.save(product);
    }

    @Override
    public List<Product> findByIdCustomer(Long idCustomer){
        Optional<List<Product>> listProducts = productRepository.findByIdCustomer(idCustomer);
        return listProducts
                .flatMap(list -> listProducts)
                .orElseThrow(() -> new IllegalStateException("No products were found with that customer id"));
    }

    private Boolean exist(Product product){
        return productRepository
                .findByCodeAndDescription(product.getCode(), product.getDescription())
                .isPresent();
    }
}
