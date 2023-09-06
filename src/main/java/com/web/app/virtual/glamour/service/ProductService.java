package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ProductDTO;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO) throws NotFoundException;

    List<ProductDTO> getAllProducts() throws NotFoundException;

    Page<ProductDTO> getAllProductsPageable(Pageable pageable) throws NotFoundException;

    ProductDTO getProductById(Long productId) throws NotFoundException;

    ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws NotFoundException;

    ResponseEntity<ResponseMessage> listedOption(Long productId) throws NotFoundException;

    ResponseEntity<ResponseMessage> deleteProduct(Long productId) throws NotFoundException;



}
