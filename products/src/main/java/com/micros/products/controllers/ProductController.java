package com.micros.products.controllers;

import com.micros.products.models.BaseResponse;
import com.micros.products.models.ProductRequest;
import com.micros.products.models.ProductResponse;
import com.micros.products.models.ProductUpdateRequest;
import com.micros.products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Crear un nuevo producto (Solo admin)
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado exitosamente");
    }


    // Obtener todos los productos (Cualquier usuario con rol admin o user)
    @GetMapping
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Obtener un producto por su ID (Cualquier usuario con rol admin o user)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest productUpdateRequest) {
        productService.updateProduct(id, productUpdateRequest);
        return ResponseEntity.ok("Producto actualizado exitosamente");
    }

    // Eliminar un producto (Solo admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }

    // Verificar si los productos están en stock (Cualquier usuario con rol admin o user)
    @PostMapping("/in-stock")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<BaseResponse> areProductsInStock(@RequestBody List<ProductRequest> products) {
        BaseResponse response = productService.areInStock(products);
        return ResponseEntity.ok(response);
    }

    // Obtener una lista de productos por ID (Cualquier usuario con rol admin o user)
    @PostMapping("/list")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<List<ProductResponse>> getProductsList(@RequestBody List<ProductRequest> products) {
        List<ProductResponse> productList = productService.getProductsList(products);
        return ResponseEntity.ok(productList);
    }

    @PostMapping("/in-stock/buy")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<List<ProductResponse>> getProductsListAndUpdateStock(@RequestBody List<ProductRequest> products) {
        List<ProductResponse> productList = productService.getProductsAndUpdateStock(products);
        return ResponseEntity.ok(productList);
    }
}