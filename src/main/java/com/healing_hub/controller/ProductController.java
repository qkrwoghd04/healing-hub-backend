package com.healing_hub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healing_hub.dto.Product;
import com.healing_hub.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.healing_hub.service.ProductService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    /* 모든 상품 가지고 오기 */
    @GetMapping
    public List<Product> getProducts() {
        return productRepository.getAllProducts();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product addProduct(@RequestPart("product") String productJson,
                              @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);
        return productService.uploadProductWithImage(imageFile, product);
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
