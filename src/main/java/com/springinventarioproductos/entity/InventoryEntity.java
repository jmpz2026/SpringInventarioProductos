package com.springinventarioproductos.entity;

import com.springinventarioproductos.dto.product.ProductResponseDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private List<ProductResponseDTO> products;
}
