package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.HttpGlobalResponse;
import com.springinventarioproductos.dto.MessageResponseDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.dto.product.ProductRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import com.springinventarioproductos.helper.ConvertHelper;
import com.springinventarioproductos.repository.InventoryRepository;
import com.springinventarioproductos.repository.MessageRepository;
import com.springinventarioproductos.repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ConvertHelper convertHelper;

    private final InventoryRepository inventoryRepository;

    public HttpGlobalResponse<ProductResponseDTO> createProduct(ProductRequestDTO productRequestDTO) {
        InventoryEntity inventoryEntity = inventoryRepository.findById(productRequestDTO.getInventoryId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND)
        );

        ProductEntity productEntity = convertHelper.convertProductRequestDtoToProductEntity(productRequestDTO,inventoryEntity);
        ProductResponseDTO productResponseDTO = convertHelper.ConvertProductEntityToProductResponseDto(productEntity);

        productRepository.save(productEntity);

        HttpGlobalResponse<ProductResponseDTO> httpGlobalResponse = new HttpGlobalResponse<>();
        httpGlobalResponse.setData(productResponseDTO);
        httpGlobalResponse.setMessage(MessageRepository.INVENTORY_CREATED);


        return httpGlobalResponse;
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
