package com.web.app.virtual.glamour.repository;

import com.web.app.virtual.glamour.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionsRepository extends JpaRepository<ProductStock, Long> {

}
