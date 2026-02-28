package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.MessageResponseDTO;
import com.springinventarioproductos.dto.product.ProductRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import com.springinventarioproductos.repository.MessageRepository;
import com.springinventarioproductos.repository.ProductRepository;
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
        return ProductEntity.builder().id(rs.getLong("id")).productName(rs.getString("product_name")).quantity(rs.getInt("quantity")).inventoryId(rs.getLong("inventory_id")).build();
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

    public ProductResponseDTO getProductById(long id){
        try{
            ProductEntity productEntity = jdbcTemplate.queryForObject(ProductRepository.SELECT_PRODUCT,productMapper,id);

            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setId(productEntity.getId());
            productResponseDTO.setProductName(productEntity.getProductName());
            productResponseDTO.setQuantity(productEntity.getQuantity());
            productResponseDTO.setInventoryId(productEntity.getInventoryId());

            return productResponseDTO;
        } catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND);
        }
    }

    public MessageResponseDTO addProduct(long id, int quantity) {
        try{
            ProductEntity productEntity = jdbcTemplate.queryForObject(ProductRepository.SELECT_PRODUCT,productMapper,id);
            MessageResponseDTO messageResponseDTO = new MessageResponseDTO();

            if (quantity <= 0) {
                messageResponseDTO.setMessage(MessageRepository.INCORRECT_AMOUNT);
                return messageResponseDTO;
            }

            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setId(productEntity.getId());
            productResponseDTO.setProductName(productEntity.getProductName());
            productResponseDTO.setQuantity(productEntity.getQuantity());
            productResponseDTO.setInventoryId(productEntity.getInventoryId());


            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        ProductRepository.UPDATE_PRODUCT,
                        Statement.RETURN_GENERATED_KEYS
                );

                preparedStatement.setInt(1,productResponseDTO.getQuantity() + quantity);
                preparedStatement.setLong(2,productResponseDTO.getId());
                return preparedStatement;
            }, keyHolder);

            messageResponseDTO.setMessage(messageProductAdded(id, quantity));

            return messageResponseDTO;
        } catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND);
        }
    }

    public MessageResponseDTO removeProduct(long id, int quantity) {
        try{
            ProductEntity productEntity = jdbcTemplate.queryForObject(ProductRepository.SELECT_PRODUCT,productMapper,id);
            MessageResponseDTO messageResponseDTO = new MessageResponseDTO();

            if (productEntity.getQuantity() < quantity) {
                messageResponseDTO.setMessage(MessageRepository.PRODUCT_NOT_ENOUGH);
                return messageResponseDTO;
            }

            if (quantity <= 0) {
                messageResponseDTO.setMessage(MessageRepository.INCORRECT_AMOUNT);
                return messageResponseDTO;
            }

            if ((productEntity.getQuantity() - quantity) <= 0) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            ProductRepository.DELETE_PRODUCT,
                            Statement.RETURN_GENERATED_KEYS
                    );

                    preparedStatement.setLong(1,productEntity.getId());
                    return preparedStatement;
                }, keyHolder);

                messageResponseDTO.setMessage(MessageRepository.REMOVED_PRODUCT);
                return  messageResponseDTO;
            }

            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setId(productEntity.getId());
            productResponseDTO.setProductName(productEntity.getProductName());
            productResponseDTO.setQuantity(productEntity.getQuantity());
            productResponseDTO.setInventoryId(productEntity.getInventoryId());

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        ProductRepository.UPDATE_PRODUCT,
                        Statement.RETURN_GENERATED_KEYS
                );

                preparedStatement.setInt(1,productResponseDTO.getQuantity() - quantity);
                preparedStatement.setLong(2,productResponseDTO.getId());
                return preparedStatement;
            }, keyHolder);

            messageResponseDTO.setMessage(messageProductRemoved(id, quantity));

            return messageResponseDTO;
        } catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND);
        }
    }

    private String messageProductRemoved(long id, int quantity) {
        return "Se removieron " + quantity + " productos al item con id " + id;
    }

    private String messageProductAdded(long id, int quantity) {
        return "Se añadieron " + quantity + " productos al item con id " + id;
    }


}
