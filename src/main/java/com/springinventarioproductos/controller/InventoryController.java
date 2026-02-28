package com.springinventarioproductos.controller;

import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("{id}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Long id) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.getInventoryById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryResponseDTO);
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(inventoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryResponseDTO);
    }
}
