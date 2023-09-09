package com.web.app.virtual.glamour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "ps_stock_id")
    private ProductStock productStock;

    @ManyToOne
    @JoinColumn(name = "sc_user_id")
    private User user;
}
