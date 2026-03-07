package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import com.springinventarioproductos.helper.ConvertHelper;
import com.springinventarioproductos.repository.InventoryRepository;
import com.springinventarioproductos.repository.MessageRepository;
import com.springinventarioproductos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    private final ConvertHelper convertHelper;

    private final InventoryRepository inventoryRepository;


    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        InventoryEntity inventoryEntity = convertHelper.convertInventoryRequestDtoToInventory(inventoryRequestDTO);

        inventoryRepository.save(inventoryEntity);

        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setName(inventoryEntity.getName());
        inventoryResponseDTO.setProducts(new ArrayList<>());
        inventoryResponseDTO.setId(inventoryEntity.getId());

        return inventoryResponseDTO;
    }

    public InventoryResponseDTO getInventoryById(Long id) {
        InventoryEntity inventoryEntity = inventoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND)
        );

        List<ProductEntity> productEntity = productRepository.findByInventoryId(id);
        List<ProductResponseDTO> productResponseDTO = productEntity.stream().map(convertHelper::ConvertProductEntityToProductResponseDto).collect(Collectors.toList());

        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(inventoryEntity.getId());
        inventoryResponseDTO.setName(inventoryEntity.getName());
        inventoryResponseDTO.setProducts(productResponseDTO);

        return inventoryResponseDTO;
    }
}
