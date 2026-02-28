package com.springinventarioproductos.dto.inventory;

import com.springinventarioproductos.dto.product.ProductResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class InventoryResponseDTO {

    private String name;
    private long id;
    private List<ProductResponseDTO> products;

}
