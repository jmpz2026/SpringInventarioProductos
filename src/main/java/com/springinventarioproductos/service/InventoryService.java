package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.inventory.InventoryRequestDTO;
import com.springinventarioproductos.dto.inventory.InventoryResponseDTO;
import com.springinventarioproductos.repository.InventoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Service
public class InventoryService {

    private JdbcTemplate jdbcTemplate;

    public InventoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<InventoryResponseDTO> inventoryMapper = (rs, rowNum) ->{
        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(rs.getLong("id"));
        inventoryResponseDTO.setName(rs.getString("name"));
        return inventoryResponseDTO;
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
        return inventoryResponseDTO;
    }

}
