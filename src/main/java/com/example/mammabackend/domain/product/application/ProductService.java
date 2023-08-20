package com.example.mammabackend.domain.product.application;

import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductQuantity;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductsParam;
import com.querydsl.core.QueryResults;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    QueryResults<Product> getProducts(ProductsParam request, Pageable pageable);

    Product getProduct(Long productSq);

    Long getProductStock(Long productSq);

    List<Product> getEnableOrderProductsByProductSqs(Collection<Long> productSqs);

    void incrementProductsStock(List<ProductQuantity> productQuantities);

    void incrementCacheProductStocks(List<ProductQuantity> productQuantities);
}
