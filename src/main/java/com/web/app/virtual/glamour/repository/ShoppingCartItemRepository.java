package com.web.app.virtual.glamour.repository;

import com.web.app.virtual.glamour.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    Optional<ShoppingCartItem> findByProductStockProductStockId(Long productStockId);
}
