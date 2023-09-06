package com.web.app.virtual.glamour.controller;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ProductDTO;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Create a New Product",
            description = "Add a new product to the e-commerce catalog. Provide product details for creation."
    )
    @PostMapping("/create")
    public ProductDTO addProduct(@Valid @RequestBody ProductDTO productDTO) throws NotFoundException {
        return productService.addProduct(productDTO);
    }

    @Operation(
            summary = "Get All Products",
            description = "Retrieve a list of all available products in the e-commerce catalog."
    )
    @GetMapping("/get-all")
    public List<ProductDTO> getAllProducts() throws NotFoundException {
        return productService.getAllProducts();
    }

    @Operation(
            summary = "Get All Products Pageable",
            description = "Retrieve a list of all available products using pageable in the e-commerce catalog."
    )
    @GetMapping("/get-all-pageable")
    public Page<ProductDTO> getAllProductsPageable(@PageableDefault Pageable pageable) throws NotFoundException {
        return productService.getAllProductsPageable(pageable);
    }

    @Operation(
            summary = "Get Product by ID",
            description = "Retrieve a specific product by its unique identifier (ID)."
    )
    @GetMapping("/get/{id}")
    public ProductDTO getProductById(@PathVariable("id") Long productId) throws NotFoundException {
        return productService.getProductById(productId);
    }

    @Operation(
            summary = "Update Product Information",
            description = "Modify the details of a specific product in the catalog. Provide the product's unique identifier (ID) for updating."
    )
    @PutMapping("/update/{id}")
    public ProductDTO updateProduct(@PathVariable("id") Long productId, @Valid @RequestBody ProductDTO productDTO) throws NotFoundException {
        return productService.updateProduct(productId, productDTO);
    }

    @Operation(
            summary = "Product Listed Handle",
            description = "This controller method toggles the listing status of a product based on its identifier, allowing it to be displayed or hidden on the platform."
    )
    @PutMapping("/listed-option/{id}")
    public ResponseEntity<ResponseMessage> listedOption(@PathVariable("id") Long productId) throws NotFoundException {
        return productService.listedOption(productId);
    }

    @Operation(
            summary = "Delete Product",
            description = "Remove a specific product from the e-commerce catalog. Provide the product's unique identifier (ID) for deletion."
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable("id") Long productId) throws NotFoundException {
        return productService.deleteProduct(productId);
    }
}
