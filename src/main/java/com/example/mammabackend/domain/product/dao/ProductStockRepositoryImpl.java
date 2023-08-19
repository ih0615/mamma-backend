package com.example.mammabackend.domain.product.dao;

import static com.example.mammabackend.domain.product.domain.QProductStock.productStock;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProductStockRepositoryImpl implements ProductStockQueryDsl {

    private final JPAQueryFactory queryFactory;

    public ProductStockRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long getStockByProductSq(Long productSq) {
        return queryFactory.from(productStock)
            .select(productStock.quantity.sum().coalesce(0L))
            .where(productStock.product.productSq.eq(productSq))
            .fetchFirst();

    }
}
