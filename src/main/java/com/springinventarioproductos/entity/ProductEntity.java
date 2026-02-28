package com.springinventarioproductos.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    private Long id;

    private String productName;

    private Integer quantity;

    private Long inventoryId;

}
