package com.micros.products.controllers;

import com.micros.products.models.BaseResponse;
import com.micros.products.models.ProductRequest;
import com.micros.products.models.ProductEntity;
import com.micros.products.models.ProductResponse;
import com.micros.products.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServices productServices;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductEntity getProductById(@PathVariable Long id) {
        return productServices.getProduct(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductEntity> getAllProducts() {
        return productServices.getAllProducts();
    }
    @PostMapping("/in-stock/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProductsList(@RequestBody List<ProductRequest> products) {
        return productServices.getProductsList(products);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse areInStock(@RequestBody List<ProductRequest> products) {
        return productServices.areInStock(products);
    }
}
