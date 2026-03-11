package com.springinventarioproductos.dto.product;

import com.springinventarioproductos.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionProductRequestDTO {

    private Long productId;
    private int quantity;
    private TransactionType transactionType;
}
