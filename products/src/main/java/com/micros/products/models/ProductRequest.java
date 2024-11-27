package com.micros.products.models;

import lombok.Getter;

@Getter
public class ProductRequest {
    private Long id;
    private String name;
    private Float price;
    private Long quantity;
}
