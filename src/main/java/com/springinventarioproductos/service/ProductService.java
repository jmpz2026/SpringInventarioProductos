package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.dto.product.ProductRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import com.springinventarioproductos.repository.InventoryRepository;
import com.springinventarioproductos.repository.MessageRepository;
import com.springinventarioproductos.repository.ProductRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private JdbcTemplate jdbcTemplate;

    public ProductService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<InventoryEntity> inventoryMapper = (rs, rowNum) ->{
        return InventoryEntity.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
    };

    private final RowMapper<ProductEntity> productMapper = (rs, rowNum) ->{
        return ProductEntity.builder().id(rs.getLong("id")).productName(rs.getString("productName")).quantity(rs.getInt("quantity")).inventoryId(rs.getLong("inventoryId")).build();
    };

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    ProductRepository.INSERT_PRODUCT,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1,productRequestDTO.getProductName());
            preparedStatement.setInt(2,productRequestDTO.getQuantity());
            preparedStatement.setLong(3,productRequestDTO.getInventoryId());
            return preparedStatement;
        }, keyHolder);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(keyHolder.getKey().longValue());
        productResponseDTO.setProductName(productRequestDTO.getProductName());
        productResponseDTO.setQuantity(productRequestDTO.getQuantity());
        productResponseDTO.setInventoryId(productRequestDTO.getInventoryId());

        return productResponseDTO;
    }

    public ProductResponseDTO getProductById(int id){
        try{
            ProductEntity productEntity = jdbcTemplate.queryForObject(ProductRepository.SELECT_PRODUCT,productMapper,id);

            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setId(productEntity.getId());
            productResponseDTO.setProductName(productEntity.getProductName());
            productResponseDTO.setQuantity(productEntity.getQuantity());

            return productResponseDTO;
        } catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND);
        }
    }

}
