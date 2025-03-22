package com.healing_hub.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String price;
    private String category;
    private String description;
    private String image;
    private String popularity;
    private String productDetailUrl;
}
