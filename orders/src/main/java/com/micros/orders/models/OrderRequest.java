package com.micros.orders.models;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private Long userId;
    private List<ProductRequest> products;
}
