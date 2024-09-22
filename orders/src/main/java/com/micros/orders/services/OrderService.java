package com.micros.orders.services;

import com.micros.orders.models.*;
import com.micros.orders.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService{
    private final OrderRepository orderRepository;

    public OrderEntity getOrder(long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public OrderEntity placeOrder(OrderRequest orderRequest) {
        WebClient webClient = WebClient.create("http://localhost:8080/api");
        //validando que el usuario exista
        UserRequest userResult = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/users/{id}").build(orderRequest.getUserId()))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el usuario con ese id."))
                )
                .bodyToMono(UserRequest.class)
                .block();
        System.out.println("Usuario validado: "+ userResult.getUsername());
        //validando que todos los productos esten en stock
        BaseResponse baseResult = webClient.post()
                .uri("/products/in-stock")
                .body(Mono.just(orderRequest.getProducts()), List.class)
                .retrieve()
                .bodyToMono(BaseResponse.class).block();
        System.out.println("Stock validado: "+ !baseResult.hasErrors());
        if(baseResult==null || baseResult.hasErrors()){
            throw new IllegalArgumentException("Algunos productos no estan en stock: "+baseResult);
        }
        //Obteniendo los productos, con su id, nombre, precio, etc
        List<ProductEntity> productsResult = webClient.post()
                .uri("/products/in-stock/list")
                .body(Mono.just(orderRequest.getProducts()), List.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductEntity>>() {}).block();
        System.out.println("Productos validados: "+ productsResult);
        if(productsResult==null || productsResult.isEmpty()){
            throw new NullPointerException("No hay productos que añadir a la orden.");
        }
        List<ProductEntity> productsToSave;
        OrderEntity order = OrderEntity.builder()
                .userId(orderRequest.getUserId())
                .products(new ArrayList<>())
                .build();
        productsResult.forEach(
                productResult ->{
                    ProductEntity product = ProductEntity.builder()
                        .name(productResult.getName())
                        .price(productResult.getPrice())
                        .quantity(productResult.getQuantity())
                            .order(order)
                        .build();
                        order.getProducts().add(product);
                }
        );
        return orderRepository.save(order);
    }


}
