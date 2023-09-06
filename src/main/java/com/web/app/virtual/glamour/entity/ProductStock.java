package com.web.app.virtual.glamour.entity;

import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull
    private String color;

    @NotNull
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "ps_product_id")
    private Product product;
}
