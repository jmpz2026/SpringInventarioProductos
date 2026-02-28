package com.springinventarioproductos.controller;

import com.springinventarioproductos.dto.MessageResponseDTO;
import com.springinventarioproductos.dto.product.ProductRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
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
        public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable long id) {
            ProductResponseDTO productResponseDTO = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @PutMapping("add/{id}/{quantity}")
    public ResponseEntity<MessageResponseDTO> addProduct(@PathVariable long id, @PathVariable int quantity) {
        MessageResponseDTO messageResponseDTO = productService.addProduct(id, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
    }

    @PutMapping("remove/{id}/{quantity}")
    public ResponseEntity<MessageResponseDTO> removeProduct(@PathVariable long id, @PathVariable int quantity) {
        MessageResponseDTO messageResponseDTO = productService.removeProduct(id, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDTO);
    }
}
