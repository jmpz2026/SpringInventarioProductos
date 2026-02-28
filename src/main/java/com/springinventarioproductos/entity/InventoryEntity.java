package com.springinventarioproductos.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {

    private Long id;

    private String name;

    private List<ProductEntity> products;
}
