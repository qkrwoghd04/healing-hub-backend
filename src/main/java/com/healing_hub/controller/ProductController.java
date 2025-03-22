package com.healing_hub.controller;

import com.healing_hub.dto.Product;
import com.healing_hub.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* 모든 상품 가지고 오기 */
    @GetMapping
    public List<Product> getProducts() {
        return productRepository.getAllProducts();
    }
}
