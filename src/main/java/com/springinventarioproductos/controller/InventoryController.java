package com.springinventarioproductos.controller;

import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<InventoryResponseDTO> getInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(inventoryRequestDTO);
        return ResponseEntity.ok().body(inventoryResponseDTO);
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(inventoryRequestDTO);
        return ResponseEntity.ok().body(inventoryResponseDTO);
    }
}
