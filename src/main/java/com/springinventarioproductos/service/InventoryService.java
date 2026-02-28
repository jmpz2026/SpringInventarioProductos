package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import com.springinventarioproductos.repository.InventoryRepository;
import com.springinventarioproductos.repository.MessageRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private JdbcTemplate jdbcTemplate;

    public InventoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<InventoryEntity> inventoryMapper = (rs, rowNum) ->{
        return InventoryEntity.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
    };

    private final RowMapper<ProductEntity> productMapper = (rs, rowNum) ->{
        return ProductEntity.builder().id(rs.getLong("id")).productName(rs.getString("product_name")).quantity(rs.getInt("quantity")).inventoryId(rs.getLong("inventory_id")).build();
    };


    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    InventoryRepository.INSERT_INVENTORY,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1,inventoryRequestDTO.getName());

            return preparedStatement;
        }, keyHolder);

        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(keyHolder.getKey().longValue());
        inventoryResponseDTO.setName(inventoryRequestDTO.getName());
        inventoryResponseDTO.setProducts(new ArrayList<>());
        return inventoryResponseDTO;
    }

    public InventoryResponseDTO getInventoryById(Long id) {
        try{
            InventoryEntity inventoryEntity = jdbcTemplate.queryForObject(InventoryRepository.SELECT_INVENTORY, inventoryMapper, id);
            List<ProductEntity> productEntity = jdbcTemplate.query(InventoryRepository.SELECT_PRODUCT_BY_INVENTORY_ID,productMapper,id);

            List<ProductResponseDTO> products = productEntity.stream().map(this::convertProductEntityToDto).toList();

            InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
            inventoryResponseDTO.setId(inventoryEntity.getId());
            inventoryResponseDTO.setName(inventoryEntity.getName());
            inventoryResponseDTO.setProducts(products);

            return inventoryResponseDTO;
        } catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND);
        }
    }

    private ProductResponseDTO convertProductEntityToDto(ProductEntity productEntity) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(productEntity.getId());
        productResponseDTO.setProductName(productEntity.getProductName());
        productResponseDTO.setQuantity(productEntity.getQuantity());
        productResponseDTO.setInventoryId(productEntity.getInventoryId());
        return productResponseDTO;
    }
}
