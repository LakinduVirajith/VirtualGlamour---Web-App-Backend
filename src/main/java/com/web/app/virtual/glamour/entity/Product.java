package com.web.app.virtual.glamour.entity;

import com.web.app.virtual.glamour.enums.Category;
import com.web.app.virtual.glamour.enums.Expression;
import com.web.app.virtual.glamour.enums.Weather;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull
    private String productName;

    private String productImages;

    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category categoryType;

    @Enumerated(EnumType.STRING)
    private Expression expressionRecommendation;

    @Enumerated(EnumType.STRING)
    private Weather weatherRecommendation;

    private Boolean isListed = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductStock> productStocks;

    @ManyToOne
    @JoinColumn(name = "p_user_id")
    private User user;
}
