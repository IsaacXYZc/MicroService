package com.micros.orders.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String userId;
    private List<ProductResponse> products;

}
