package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ShoppingCartItemDTO;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartItemService {
    ResponseEntity<ResponseMessage> addToCart(Long optionalId, Integer quantity) throws NotFoundException, BadRequestException;

    ResponseEntity<ResponseMessage> updateCart(Long optionalId, Integer quantity) throws NotFoundException, BadRequestException;

    List<ShoppingCartItemDTO> getAllCartData() throws NotFoundException;

    ResponseEntity<ResponseMessage> deleteData(Long cartItemId) throws NotFoundException;
}
