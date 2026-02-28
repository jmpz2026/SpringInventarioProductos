package com.springinventarioproductos.dto.product;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private String productName;
    private Integer quantity;
    private Long inventoryId;

}
