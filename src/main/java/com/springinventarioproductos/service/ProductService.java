package com.springinventarioproductos.service;

import com.springinventarioproductos.dto.HttpGlobalResponse;
import com.springinventarioproductos.dto.MessageResponseDTO;
import com.springinventarioproductos.dto.product.ProductRequestDTO;
import com.springinventarioproductos.dto.product.ProductResponseDTO;
import com.springinventarioproductos.dto.product.TransactionProductRequestDTO;
import com.springinventarioproductos.entity.InventoryEntity;
import com.springinventarioproductos.entity.ProductEntity;
import com.springinventarioproductos.enums.TransactionType;
import com.springinventarioproductos.helper.ConvertHelper;
import com.springinventarioproductos.repository.InventoryRepository;
import com.springinventarioproductos.repository.MessageRepository;
import com.springinventarioproductos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        productRepository.save(productEntity);

        ProductResponseDTO productResponseDTO = convertHelper.ConvertProductEntityToProductResponseDto(productEntity);

        HttpGlobalResponse<ProductResponseDTO> httpGlobalResponse = new HttpGlobalResponse<>();
        httpGlobalResponse.setData(productResponseDTO);
        httpGlobalResponse.setMessage(MessageRepository.INVENTORY_CREATED);


        return httpGlobalResponse;
    }

    public HttpGlobalResponse<ProductResponseDTO> getProductById(long id){
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND)
        );

        ProductResponseDTO productResponseDTO = convertHelper.ConvertProductEntityToProductResponseDto(productEntity);

        HttpGlobalResponse<ProductResponseDTO> httpGlobalResponse = new HttpGlobalResponse<>();
        httpGlobalResponse.setData(productResponseDTO);
        httpGlobalResponse.setMessage(MessageRepository.PRODUCT_FOUND);

        return httpGlobalResponse;
    }

    public HttpGlobalResponse<MessageResponseDTO> transactionProduct(TransactionProductRequestDTO transactionProductRequestDTO) {
        ProductEntity productEntity = productRepository.findById(transactionProductRequestDTO.getProductId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.NOT_FOUND)
        );

        TransactionType transactionType = transactionProductRequestDTO.getTransactionType();
        HttpGlobalResponse<MessageResponseDTO> httpGlobalResponse = new HttpGlobalResponse<>();
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();

        switch (transactionType) {
            case TransactionType.ADD:
                productEntity.setQuantity(productEntity.getQuantity()+transactionProductRequestDTO.getQuantity());
                productRepository.save(productEntity);

                messageResponseDTO.setMessage(MessageRepository.TRANSACTION_SUCCESS);
                httpGlobalResponse.setData(messageResponseDTO);
                break;
            case TransactionType.REMOVE:
                if (productEntity.getQuantity() < transactionProductRequestDTO.getQuantity()){

                    messageResponseDTO.setMessage(MessageRepository.PRODUCT_NOT_ENOUGH);
                    httpGlobalResponse.setData(messageResponseDTO);
                    break;
                }
                if ((productEntity.getQuantity() - transactionProductRequestDTO.getQuantity()) == 0){

                    productEntity.setQuantity(0);
                    productRepository.save(productEntity);

                    messageResponseDTO.setMessage(MessageRepository.PRODUCT_REMOVED);
                    httpGlobalResponse.setData(messageResponseDTO);
                    break;
                }

                productEntity.setQuantity(productEntity.getQuantity()-transactionProductRequestDTO.getQuantity());
                productRepository.save(productEntity);

                messageResponseDTO.setMessage(MessageRepository.TRANSACTION_SUCCESS);
                httpGlobalResponse.setData(messageResponseDTO);
                break;
            default:
                messageResponseDTO.setMessage(MessageRepository.TRANSACTION_INCORRECT);
                httpGlobalResponse.setData(messageResponseDTO);
                break;
        }
        return httpGlobalResponse;
    }

}
