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
public class ProductStockDTO {

    private Long productStockId;

    @NotNull
    private String sizeName;

    private String sizeImage;

    @NotNull
    private BigDecimal price;

    private Integer discount;

    private LocalDate discountStartDate;

    private LocalDate discountEndDate;

    @NotNull
    private String color;

    @NotNull
    private Integer stockQuantity;
}
