package com.web.app.virtual.glamour.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemDTO {

    @NotNull
    private Long cartId;

    @NotNull
    private String productName;

    @NotNull
    private String sizeName;

    @NotNull
    private String color;

    private String sizeImage;

    @NotNull
    private BigDecimal price;

    private Integer discount;

    private LocalDate discountStartDate;

    private LocalDate discountEndDate;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer stockQuantity;
}
