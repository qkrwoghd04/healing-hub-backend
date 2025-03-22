package com.healing_hub.repository;

import com.healing_hub.dto.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "products";

    public ProductRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /* 모든 상품 가지고 오기 */
    public List<Product> getAllProducts() {
        ScanRequest scanRequest = ScanRequest.builder().tableName(tableName).build();
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        return scanResponse.items().stream()
                .map(item -> {
                    String id = item.get("id").s();
                    String name = item.get("name").s();
                    String price = item.get("price").s();
                    String category = item.get("category").s();
                    String description = item.get("description").s();
                    String image = item.get("image").s();
                    String popularity = item.get("popularity").s();
                    /* Null값을 가지고 았는 Product_Detail_Url이 존재 */
                    String productDetailUrl = item.get("product_detail_url") != null ? item.get("product_detail_url").s() : "";

                    return new Product(id, name, price, category, description, image, popularity, productDetailUrl);
                })
                .collect(Collectors.toList());
    }
}
