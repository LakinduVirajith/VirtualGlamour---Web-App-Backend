package com.web.app.virtual.glamour.controller;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ProductStockDTO;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.service.ProductOptionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor/product/options")
@RequiredArgsConstructor
@Tag(name = "Product Options Controller")
public class ProductOptionsController {

    private final ProductOptionsService optionsService;

    @Operation(summary = "Add a Product Option", description = "Add a new product option to an existing product.")
    @PostMapping("/add/{id}")
    public ProductStockDTO addProductOption(@PathVariable("id") Long productId, @Valid @RequestBody ProductStockDTO productStockDTO) throws NotFoundException {
        return optionsService.addProductOption(productId, productStockDTO);
    }

    @Operation(summary = "Update a Product Option", description = "Update an existing product option.")
    @PutMapping("/update/{id}")
    public ProductStockDTO updateProductOption(@PathVariable("id") Long optionId, @Valid @RequestBody ProductStockDTO productStockDTO) throws NotFoundException {
        return optionsService.updateProductOption(optionId, productStockDTO);
    }

    @Operation(summary = "Get all product options", description = "Retrieve all product options for a given product.")
    @GetMapping("/get-all/{id}")
    public List<ProductStockDTO> getAllProductOption(@PathVariable("id") Long productId) throws NotFoundException {
        return optionsService.getAllProductOption(productId);
    }

    @Operation(summary = "Get a product option", description = "Retrieve a product option by its ID.")
    @GetMapping("/get/{id}")
    public ProductStockDTO getProductOption(@PathVariable("id") Long optionId) throws NotFoundException {
        return optionsService.getProductOption(optionId);
    }

    @Operation(summary = "Remove a product option", description = "Remove a product option from the existing product.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> removeProductOption(@PathVariable("id") Long optionId) throws NotFoundException, BadRequestException {
        return optionsService.removeProductOption(optionId);
    }
}
