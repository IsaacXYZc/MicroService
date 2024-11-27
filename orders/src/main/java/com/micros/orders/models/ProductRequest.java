package com.micros.orders.models;

import lombok.Getter;
import lombok.Data;


@Getter
@Data
public class ProductRequest {
    private Long id;
    private Long quantity;
}
