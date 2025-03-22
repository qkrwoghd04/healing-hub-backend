package com.healing_hub.service;

import com.healing_hub.dto.Product;
import com.healing_hub.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final S3Client s3Client;
    private final ProductRepository productRepository;
    private String BUCKET_NAME = "healing-hub-images";

    @Value("${aws.region}")
    private String region;

    public Product uploadProductWithImage(MultipartFile imageFile, Product product) throws IOException {
        String id = UUID.randomUUID().toString();
        product.setId(id);

        String imageUrl;

        if (imageFile != null && !imageFile.isEmpty()) {
            String key = id + ".jpg";

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .contentType(imageFile.getContentType())
                            .build(),
                    RequestBody.fromBytes(imageFile.getBytes())
            );

            imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    BUCKET_NAME, region, key);
        } else {
            // 기본 이미지 처리 (버킷에 default.jpg가 있어야 함)
            imageUrl = String.format("https://%s.s3.%s.amazonaws.com/default.jpg",
                    BUCKET_NAME, region);
        }

        product.setImage(imageUrl);

        return productRepository.saveProduct(product);
    }
}
