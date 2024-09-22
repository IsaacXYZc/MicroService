package com.micros.products.services;

import com.micros.products.models.BaseResponse;
import com.micros.products.models.ProductRequest;
import com.micros.products.models.ProductEntity;
import com.micros.products.models.ProductResponse;
import com.micros.products.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServices {
    private final ProductRepository productRepository;

    public ProductEntity getProduct(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public List<ProductEntity> getAllProducts(){
        System.out.println(productRepository.findAll());
        return productRepository.findAll();
    }

    public BaseResponse areInStock(List<ProductRequest> products) {
        var errorList = new ArrayList<String>();
        products.forEach(product -> {
           if(productRepository.findById(product.getId()).isEmpty()){
               errorList.add("Producto con ID " + product.getId() + " no encontrado.");
           }
        });
        return !errorList.isEmpty() ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);
    }

    public List<ProductResponse> getProductsList(List<ProductRequest> products) {
        List<ProductResponse> productsList = new ArrayList<>();
        products.forEach(product -> {
            ProductEntity productFinded = productRepository.findById(product.getId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            productsList.add(new ProductResponse(product.getId(),productFinded.getName(),productFinded.getPrice(),product.getQuantity()));
        });
        return productsList;
    }
}
