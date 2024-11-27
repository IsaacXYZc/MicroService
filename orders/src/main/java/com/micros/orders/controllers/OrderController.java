package com.micros.orders.controllers;

import com.micros.orders.models.OrderEntity;
import com.micros.orders.models.OrderRequest;
import com.micros.orders.models.OrderResponse;
import com.micros.orders.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Obtener un pedido por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or #userId == authentication.principal.username")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest, Authentication authentication) {
        // Obtener el userId del contexto de autenticación
        String userId = getUserIdFromAuthentication(authentication);


        // Asignar el userId al request antes de procesarlo
        orderRequest.setUserId(userId);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        OrderResponse order = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // Obtener todos los pedidos de un usuario
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('admin') or #userId == authentication.principal.username")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // Cancelar un pedido por ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or #userId == authentication.principal.username")
    public ResponseEntity<String> cancelOrder(@PathVariable long id, Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        orderService.cancelOrder(id, userId);
        return ResponseEntity.ok("Order cancelled successfully.");
    }

    private String getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("No se encontró autenticación en el contexto");
        }

        // Verificar si el Principal es un Jwt
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal();
            return jwt.getClaimAsString("sub"); // "sub" contiene el ID del usuario en Keycloak
        }

        // Manejo para otros tipos de principal, si es necesario
        throw new IllegalStateException("No se pudo extraer el ID del usuario del objeto Authentication");
    }
}