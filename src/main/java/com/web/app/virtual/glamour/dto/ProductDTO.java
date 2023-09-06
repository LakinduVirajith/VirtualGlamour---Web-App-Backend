package com.web.app.virtual.glamour.dto;

import com.web.app.virtual.glamour.entity.ProductStock;
import com.web.app.virtual.glamour.enums.Category;
import com.web.app.virtual.glamour.enums.Expression;
import com.web.app.virtual.glamour.enums.Weather;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long productId;

    @NotNull
    private String productName;

    private String productImages;

    private String description;

    @NotNull
    private BigDecimal price;

    private BigDecimal discountAmount;

    private LocalDate discountStartDate;

    private LocalDate discountEndDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category categoryType;

    @Enumerated(EnumType.STRING)
    private Expression expressionRecommendation;

    @Enumerated(EnumType.STRING)
    private Weather weatherRecommendation;

    private Boolean isListed = true;

    private List<ProductStockDTO> productStockDTOS;
}
