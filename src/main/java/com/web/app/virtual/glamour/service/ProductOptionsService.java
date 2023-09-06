package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ProductStockDTO;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductOptionsService {
    ProductStockDTO addProductOption(Long productId, ProductStockDTO productStockDTO) throws NotFoundException;

    ProductStockDTO updateProductOption(Long optionId, ProductStockDTO productStockDTO) throws NotFoundException;

    List<ProductStockDTO> getAllProductOption(Long productId) throws NotFoundException;

    ProductStockDTO getProductOption(Long optionId) throws NotFoundException;

    ResponseEntity<ResponseMessage> removeProductOption(Long optionId) throws NotFoundException, BadRequestException;
}
