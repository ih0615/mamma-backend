package com.example.mammabackend.domain.product.dao;

import com.example.mammabackend.domain.product.domain.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long>,
    ProductStockQueryDsl {

}
