package com.springinventarioproductos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "productName")
    private String productName;

    @Column(nullable = false, name = "quantity")
    private Integer quantity;

    @Column(nullable = false, name = "inventoryId")
    private Long inventoryId;

}
