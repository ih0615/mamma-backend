package com.example.mammabackend.domain.product.api;

import com.example.mammabackend.domain.product.application.ProductService;
import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductView;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductsParam;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductsView;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.common.Response;
import com.example.mammabackend.global.common.Response.Body;
import com.querydsl.core.QueryResults;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final Response response;
    private final Helper helper = Helper.getInstance();

    @GetMapping
    public ResponseEntity<Body> getProducts(ProductsParam request,
        @PageableDefault(sort = "productSq", direction = Direction.ASC) Pageable pageable) {

        QueryResults<Product> qr = productService.getProducts(request, pageable);
        List<ProductsView> results = qr.getResults().stream()
            .map(ProductsView::fromEntity)
            .toList();

        return response.ok(
            new QueryResults<>(results, qr.getLimit(), qr.getOffset(), qr.getTotal()));
    }

    @GetMapping("/{productSq}")
    public ResponseEntity<Body> getProducts(@PathVariable Long productSq) {

        Product product = productService.getProduct(productSq);
        Long productStock = productService.getProductStock(productSq);

        return response.ok(ProductView.fromEntity(product, productStock));
    }

}
