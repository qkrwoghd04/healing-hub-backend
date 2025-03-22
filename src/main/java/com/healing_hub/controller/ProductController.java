package com.healing_hub.controller;

import com.healing_hub.dto.Product;
import com.healing_hub.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productRepository.saveProduct(product);
    }

    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        product.setId(id);
        return productRepository.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productRepository.deleteProduct(id);
    }

}
