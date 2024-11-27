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

            //CREANDO PRODUCTOS DE PRUEBA
            /*
            productRepository.save(ProductEntity.builder()
                    .description("Papa")
                    .name("Papa por libra")
                    .price(2000.99F)
                    .build());
            productRepository.save(ProductEntity.builder()
                    .description("Yuca")
                    .name("Yuca por libra")
                    .price(1300.00F)
                    .build());
            System.out.println("productos guardados."); */
        };

    }
}
