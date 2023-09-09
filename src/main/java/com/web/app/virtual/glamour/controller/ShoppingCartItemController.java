package com.web.app.virtual.glamour.controller;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ShoppingCartItemDTO;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.service.ShoppingCartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart Controllers")
public class ShoppingCartItemController {

    private final ShoppingCartItemService cartService;

    @Operation(summary = "Add a product option to the cart", description = "Add a product option to the user's shopping cart using the provided optional ID and quantity.")
    @PostMapping("/add/{id}")
    public ResponseEntity<ResponseMessage> addToCart(@PathVariable("id") Long optionalId, @RequestBody Integer quantity) throws NotFoundException, BadRequestException {
        return cartService.addToCart(optionalId, quantity);
    }

    @Operation(summary = "Update the quantity of a product option in the cart", description = "Update the quantity of a product option in the user's shopping cart using the provided optional ID and quantity.")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateCart(@PathVariable("id") Long optionalId, @RequestBody Integer quantity) throws NotFoundException, BadRequestException {
        return cartService.updateCart(optionalId, quantity);
    }

    @Operation(summary = "Get all cart data", description = "Retrieve all data related to the user's shopping cart.")
    @GetMapping("/get/all")
    public List<ShoppingCartItemDTO> getAllCartData() throws NotFoundException {
        return cartService.getAllCartData();
    }

    @Operation(summary = "Delete a product option from the cart", description = "Delete a product option from the user's shopping cart using the provided optional ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteData(@PathVariable("id") Long cartItemId) throws NotFoundException {
        return cartService.deleteData(cartItemId);
    }
}
