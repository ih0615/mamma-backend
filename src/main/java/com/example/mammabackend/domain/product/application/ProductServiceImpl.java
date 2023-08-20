package com.example.mammabackend.domain.product.application;

import static com.example.mammabackend.domain.product.domain.QProduct.product;

import com.example.mammabackend.domain.product.dao.ProductRepository;
import com.example.mammabackend.domain.product.dao.ProductStockRepository;
import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.domain.product.domain.ProductStock;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductQuantity;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductsParam;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.exception.ResponseCodes;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Helper helper = Helper.getInstance();
    private final String PRODUCT_STOCK_KEY = "product:stock:";

    @Override
    public QueryResults<Product> getProducts(ProductsParam request, Pageable pageable) {

        List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(request.getKeyword())) {
            where.add(product.name.contains(request.getKeyword()));
        }
        if (request.getIsSale() != null) {
            where.add(product.isSale.eq(request.getIsSale()));
        }

        List<OrderSpecifier> sort = helper.convertPageableToOrderSpecifier(pageable);

        return productRepository.findAllPaged(where, pageable.getOffset(), pageable.getPageSize(),
            sort);
    }

    @Override
    public Product getProduct(Long productSq) {
        return productRepository.findByProductSqAndIsUsedIsTrue(productSq)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));
    }

    @Override
    public Long getProductStock(Long productSq) {

        String redisKey = PRODUCT_STOCK_KEY + productSq;

        return Optional.ofNullable(redisTemplate.opsForValue().get(redisKey))
            .map(Long::valueOf)
            .orElseGet(() -> {
                Long dbProductStock = productStockRepository.getStockByProductSq(productSq);
                redisTemplate.opsForValue().set(redisKey, String.valueOf(dbProductStock));
                return dbProductStock;
            });
    }

    @Override
    public List<Product> getEnableOrderProductsByProductSqs(Collection<Long> productSqs) {
        return productRepository.findAllByProductSqInAndIsSaleIsTrueAndIsUsedIsTrue(
            productSqs);
    }

    @Transactional
    @Override
    public void incrementProductsStock(List<ProductQuantity> productQuantities) {

        List<ProductQuantity> completedProductQuantity = new ArrayList<>();

        try {
            productQuantities.forEach(productQuantity -> {
                incrementCacheProductStock(productQuantity.getProduct().getProductSq(),
                    productQuantity.getQuantity());
                completedProductQuantity.add(productQuantity);
            });

            List<ProductStock> productStocks = generateProductStocks(productQuantities);

            productStockRepository.saveAll(productStocks);

        } catch (Exception e) {
            List<ProductQuantity> rollbackList = completedProductQuantity.stream()
                .map(ProductQuantity::toNegate).toList();
            rollbackList.forEach(productQuantity -> incrementCacheProductStock(
                productQuantity.getProduct().getProductSq(),
                productQuantity.getQuantity()));
            throw e;
        }
    }

    @Override
    public void incrementCacheProductStocks(List<ProductQuantity> productQuantities) {
        productQuantities.forEach(productQuantity -> incrementCacheProductStock(
            productQuantity.getProduct().getProductSq(), productQuantity.getQuantity()));
    }

    private void incrementCacheProductStock(Long productSq, Long quantity) {

        String redisKey = PRODUCT_STOCK_KEY + productSq;

        Long productStock = getProductStock(productSq);
        if (productStock <= quantity) {
            throw new IllegalStateException(ResponseCodes.PROCESS_INVALID);
        }
        redisTemplate.opsForValue().increment(redisKey, quantity);
    }

    private List<ProductStock> generateProductStocks(List<ProductQuantity> productQuantities) {

        return productQuantities.stream()
            .map(ProductQuantity::toEntity)
            .toList();
    }
}
