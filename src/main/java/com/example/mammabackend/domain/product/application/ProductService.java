package com.example.mammabackend.domain.product.application;

import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductsParam;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    QueryResults<Product> getProducts(ProductsParam request, Pageable pageable);

    Product getProduct(Long productSq);

    Long getProductStock(Long productSq);
}
