package com.micros.products;

import com.micros.products.models.ProductEntity;
import com.micros.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ProductsApplication {

    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {

        SpringApplication.run(ProductsApplication.class, args);

    }

    @Bean
    CommandLineRunner init(ProductRepository productRepository) {
        return args -> {

            //CREANDO AL USUARIO ADMIN
            productRepository.save(ProductEntity.builder()
                    .description("Product Description")
                    .name("Example Product")
                    .price(19.99F)
                    .build());
            productRepository.save(ProductEntity.builder()
                    .description("Product Description 2")
                    .name("Example Product 2")
                    .price(40.00F)
                    .build());
            System.out.println("productos guardados.");
        };

    }
}
