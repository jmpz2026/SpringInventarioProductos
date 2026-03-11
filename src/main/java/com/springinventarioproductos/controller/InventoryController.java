package com.springinventarioproductos.controller;

import com.springinventarioproductos.dto.HttpGlobalResponse;
import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpGlobalResponse<InventoryResponseDTO>> getInventoryById(@PathVariable Long id) {
        HttpGlobalResponse<InventoryResponseDTO> response = inventoryService.getInventoryById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @PostMapping
    public ResponseEntity<HttpGlobalResponse<InventoryResponseDTO>> createInventory(@Valid @RequestBody InventoryRequestDTO inventoryRequestDTO) {
        HttpGlobalResponse<InventoryResponseDTO> response = inventoryService.createInventory(inventoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
