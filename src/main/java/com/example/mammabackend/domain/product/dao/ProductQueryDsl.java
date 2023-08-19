package com.example.mammabackend.domain.product.dao;

import com.example.mammabackend.domain.product.domain.Product;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import java.util.Collection;

public interface ProductQueryDsl {

    QueryResults<Product> findAllPaged(Collection<Predicate> where, long offset, int pageSize,
        Collection<OrderSpecifier> sort);
}
