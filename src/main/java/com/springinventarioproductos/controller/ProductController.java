package com.springinventarioproductos.controller;

import com.springinventarioproductos.dto.HttpGlobalResponse;
import com.springinventarioproductos.dto.MessageResponseDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.dto.product.ProductRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.dto.product.TransactionProductRequestDTO;
import com.springinventarioproductos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("{id}")
        public ResponseEntity<HttpGlobalResponse<ProductResponseDTO>> getProductById(@PathVariable long id) {
            HttpGlobalResponse<ProductResponseDTO> productResponseDTO = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<HttpGlobalResponse<ProductResponseDTO>> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        HttpGlobalResponse<ProductResponseDTO> productResponseDTO = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @PutMapping
    public ResponseEntity<HttpGlobalResponse<MessageResponseDTO>> addProduct(@RequestBody TransactionProductRequestDTO transactionProductRequestDTO) {
        HttpGlobalResponse<MessageResponseDTO> httpGlobalResponse = productService.transactionProduct(transactionProductRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(httpGlobalResponse);
    }
}
