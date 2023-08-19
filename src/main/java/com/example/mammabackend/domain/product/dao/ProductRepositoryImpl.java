package com.example.mammabackend.domain.product.dao;

import static com.example.mammabackend.domain.product.domain.QProduct.product;

import com.example.mammabackend.domain.product.domain.Product;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductQueryDsl {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public QueryResults<Product> findAllPaged(Collection<Predicate> where, long offset,
        int limit, Collection<OrderSpecifier> sort) {

        Long total = queryFactory
            .from(product)
            .select(product.productSq.count().coalesce(0L))
            .where(where.toArray(new Predicate[0]))
            .fetchFirst();

        List<Product> results = queryFactory
            .from(product)
            .select(product)
            .where(where.toArray(new Predicate[0]))
            .offset(offset)
            .limit(limit)
            .orderBy(sort.toArray(OrderSpecifier[]::new))
            .fetch();

        return new QueryResults<>(results, (long) limit, offset, total);
    }
}
