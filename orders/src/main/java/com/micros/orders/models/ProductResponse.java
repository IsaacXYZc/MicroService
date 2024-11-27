package com.micros.orders.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Float price;
    private Long quantity;
}
