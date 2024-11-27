package com.micros.orders.feign;

import com.micros.orders.models.BaseResponse;
import com.micros.orders.models.ProductEntity;
import com.micros.orders.models.ProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8080/api/products")
public interface ProductClient {

    @PostMapping("/in-stock")
    BaseResponse checkProductsInStock(@RequestBody List<ProductRequest> productIds);

    @PostMapping("/in-stock/buy")
    List<ProductEntity> getProductsInStock(@RequestBody List<ProductRequest> productIds);
}