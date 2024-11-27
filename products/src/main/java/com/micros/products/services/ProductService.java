package com.micros.products.services;

import com.micros.products.models.*;
import com.micros.products.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Obtener un producto por su ID
    public ProductResponse getProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    // Obtener todos los productos
    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        products.forEach(product -> productResponses.add(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity())));
        return productResponses;
    }

    // Verificar si los productos están en stock
    public BaseResponse areInStock(List<ProductRequest> products) {
        var errorList = new ArrayList<String>();
        products.forEach(product -> {
            ProductEntity productEntity = productRepository.findById(product.getId())
                    .orElse(null);
            if (productEntity == null) {
                errorList.add("Producto con ID " + product.getId() + " no encontrado.");
            } else if (productEntity.getQuantity() < product.getQuantity()) {
                errorList.add("Producto con ID " + product.getId() + " no tiene suficiente cantidad en stock.");
            }
        });
        return !errorList.isEmpty() ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);
    }

    // Obtener una lista de productos con cantidades
    public List<ProductResponse> getProductsList(List<ProductRequest> products) {
        List<ProductResponse> productList = new ArrayList<>();
        products.forEach(product -> {
            ProductEntity productEntity = productRepository.findById(product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + product.getId() + " no encontrado"));
            productList.add(new ProductResponse(product.getId(), productEntity.getName(), productEntity.getPrice(), product.getQuantity()));
        });
        return productList;
    }

    // Obtener un producto por su ID
    public ProductResponse getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    // Obtener productos y reducir el inventario correspondiente
    public List<ProductResponse> getProductsAndUpdateStock(List<ProductRequest> products) {
        List<ProductResponse> productList = new ArrayList<>();

        products.forEach(product -> {
            ProductEntity productEntity = productRepository.findById(product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + product.getId() + " no encontrado"));

            // Verificar si hay suficiente stock
            if (productEntity.getQuantity() < product.getQuantity()) {
                throw new IllegalArgumentException("Producto con ID " + product.getId() + " no tiene suficiente stock disponible.");
            }

            // Reducir el inventario
            productEntity.setQuantity(productEntity.getQuantity() - product.getQuantity());
            productRepository.save(productEntity);

            // Agregar a la respuesta
            productList.add(new ProductResponse(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), product.getQuantity()));
        });

        return productList;
    }

    // Crear un nuevo producto
    public void createProduct(ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productRequest.getName());
        productEntity.setPrice(productRequest.getPrice());
        productEntity.setQuantity(productRequest.getQuantity());
        System.out.println(productEntity);
        productRepository.save(productEntity);
    }

    // Actualizar un producto existente
    public void updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        productEntity.setName(productUpdateRequest.getName());
        productEntity.setPrice(productUpdateRequest.getPrice());
        productEntity.setQuantity(productUpdateRequest.getQuantity());
        productRepository.save(productEntity);
    }

    // Eliminar un producto
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        productRepository.delete(productEntity);
    }
}