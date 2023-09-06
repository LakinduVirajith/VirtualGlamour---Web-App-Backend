package com.web.app.virtual.glamour.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockDTO {

    private Long productStockId;

    @NotNull
    private String sizeName;

    @NotNull
    private String color;

    @NotNull
    private Integer stockQuantity;
}
