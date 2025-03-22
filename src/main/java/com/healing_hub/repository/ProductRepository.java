package com.healing_hub.repository;

import com.healing_hub.dto.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
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

    /* 상품 추가 */
    public Product saveProduct(Product product) {
        product.setId(UUID.randomUUID().toString());
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("id", AttributeValue.builder().s(product.getId()).build());
        itemValues.put("name", AttributeValue.builder().s(product.getName()).build());
        itemValues.put("price", AttributeValue.builder().s(product.getPrice()).build());
        itemValues.put("category", AttributeValue.builder().s(product.getCategory()).build());
        itemValues.put("description", AttributeValue.builder().s(product.getDescription()).build());
        itemValues.put("image", AttributeValue.builder().s(product.getImage()).build());
        itemValues.put("popularity", AttributeValue.builder().s(product.getPopularity()).build());
        itemValues.put("product_detail_url", AttributeValue.builder().s(product.getProductDetailUrl()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        dynamoDbClient.putItem(request);
        return product;
    }

    /* 상품 업데이트 */
    public Product updateProduct(Product product) {
        Map<String, AttributeValueUpdate> updatedValues = new HashMap<>();
        if (product.getName() != null)
            updatedValues.put("name", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(product.getName()).build())
                    .action(AttributeAction.PUT).build());
        if (product.getPrice() != null)
            updatedValues.put("price", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(product.getPrice()).build())
                    .action(AttributeAction.PUT).build());
        if (product.getCategory() != null)
            updatedValues.put("category", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(product.getCategory()).build())
                    .action(AttributeAction.PUT).build());
        if (product.getDescription() != null)
            updatedValues.put("description", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(product.getDescription()).build())
                    .action(AttributeAction.PUT).build());
        if (product.getImage() != null)
            updatedValues.put("image", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(product.getImage()).build())
                    .action(AttributeAction.PUT).build());
        if (product.getPopularity() != null)
            updatedValues.put("popularity", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(product.getPopularity()).build())
                    .action(AttributeAction.PUT).build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(product.getId()).build()))
                .attributeUpdates(updatedValues)
                .returnValues(ReturnValue.ALL_NEW)
                .build();

        dynamoDbClient.updateItem(request);
        return product;
    }

    public void deleteProduct(String id) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();

        dynamoDbClient.deleteItem(request);
    }
}
