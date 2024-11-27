package com.micros.products.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String name;
    private Long quantity;
    @Column(nullable = false)
    private Float price;

}
