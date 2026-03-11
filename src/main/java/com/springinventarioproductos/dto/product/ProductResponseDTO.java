package com.springinventarioproductos.dto.product;

import lombok.Data;

@Data
public class ProductResponseDTO {

    private Long id;
    private String productName;
    private Integer quantity;
    private Long inventoryId;

}
