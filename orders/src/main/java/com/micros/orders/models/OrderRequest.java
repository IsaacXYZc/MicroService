package com.micros.orders.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;



import java.util.List;

@Getter
@Setter
@Data
public class OrderRequest {
    private String userId;
    private List<ProductRequest> products;
}
