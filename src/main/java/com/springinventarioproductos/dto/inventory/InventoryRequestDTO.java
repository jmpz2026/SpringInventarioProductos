package com.springinventarioproductos.dto.inventory;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class InventoryRequestDTO {

    @NotBlank(message = "Name Invalid")
    private String name;

}
