package com.springinventarioproductos.dto.product;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private long id;
    private String productName;
    private Integer quantity;
    private Long inventoryId;

}
