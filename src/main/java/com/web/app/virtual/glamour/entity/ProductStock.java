package com.web.app.virtual.glamour.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "ps_product_id")
    private Product product;

    @OneToOne(mappedBy = "productStock", cascade = CascadeType.ALL)
    private ShoppingCartItem shoppingCart;
}
