package com.springinventarioproductos.helper;

import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ConvertHelper {

    // Metodos Utiles
    public InventoryEntity convertInventoryRequestDtoToInventory(InventoryRequestDTO inventoryRequestDTO) {
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setName(inventoryRequestDTO.getName());
        inventoryEntity.setProducts(new ArrayList<>());
        return inventoryEntity;
    }


    public ProductResponseDTO ConvertProductEntityToProductResponseDto(ProductEntity productEntity) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductName(productEntity.getProductName());
        productResponseDTO.setQuantity(productEntity.getQuantity());
        productResponseDTO.setInventoryId(productEntity.getInventoryId());
        return productResponseDTO;
    }
}
