package com.micros.orders.services;

import com.micros.orders.feign.ProductClient;
import com.micros.orders.feign.UserClient;
import com.micros.orders.models.*;
import com.micros.orders.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;       // Cliente Feign para usuarios
    private final ProductClient productClient; // Cliente Feign para productos

    public OrderService(OrderRepository orderRepository, UserClient userClient, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    public OrderResponse getOrder(long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden con el ID " + id + " no encontrada"));
        return convertToOrderResponse(order);
    }

    public OrderResponse  placeOrder(OrderRequest orderRequest) {

        // Validar que los productos están en stock
        BaseResponse baseResult = productClient.checkProductsInStock(orderRequest.getProducts());
        System.out.println("Stock validado: " + !baseResult.hasErrors());
        if (baseResult.hasErrors()) {
            throw new IllegalArgumentException("Algunos productos no están en stock: " + baseResult);
        }

        // Obtener detalles de los productos
        List<ProductEntity> productsResult = productClient.getProductsInStock(orderRequest.getProducts());
        System.out.println("Productos validados: " + productsResult);
        if (productsResult == null || productsResult.isEmpty()) {
            throw new NullPointerException("No hay productos que añadir a la orden.");
        }

        // Crear la orden y asociar los productos
        OrderEntity order = OrderEntity.builder()
                .userId(orderRequest.getUserId())
                .products(new ArrayList<>())
                .build();

        productsResult.forEach(productResult -> {
            ProductEntity product = ProductEntity.builder()
                    .name(productResult.getName())
                    .price(productResult.getPrice())
                    .quantity(productResult.getQuantity())
                    .order(order)
                    .build();
            order.getProducts().add(product);
        });

        return convertToOrderResponse(orderRepository.save(order));
    }

    // Obtener todos los pedidos de un usuario
    public List<OrderResponse> getOrdersByUserId(String userId) {
        List<OrderEntity> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(order -> orderResponses.add(convertToOrderResponse(order)));
        return orderResponses;
    }

    // Cancelar un pedido
    public void cancelOrder(long id, String userId) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + id + " not found"));

        if (!order.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to cancel this order");
        }

        // Cancelar la orden (restaurar productos si es necesario)
        orderRepository.delete(order);
    }

    private OrderResponse convertToOrderResponse(OrderEntity order) {
        List<ProductResponse> productResponses = new ArrayList<>();
        order.getProducts().forEach(product ->
                productResponses.add(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity()))
        );
        return new OrderResponse(order.getId(), order.getUserId(), productResponses);
    }

}
