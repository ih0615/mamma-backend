package com.example.mammabackend.domain.product.dao;

import com.example.mammabackend.domain.product.domain.Product;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryDsl {

    Optional<Product> findByProductSqAndIsUsedIsTrue(Long productSq);
    List<Product> findAllByProductSqInAndIsSaleIsTrueAndIsUsedIsTrue(Collection<Long> productSqs);

}
