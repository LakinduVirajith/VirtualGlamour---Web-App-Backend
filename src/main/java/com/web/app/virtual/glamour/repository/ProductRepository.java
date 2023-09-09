package com.web.app.virtual.glamour.repository;

import com.web.app.virtual.glamour.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByUserUserId(Long userId, Pageable pageable);
    List<Product> findAllByUserUserId(Long userId);
}
