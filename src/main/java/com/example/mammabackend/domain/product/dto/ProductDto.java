package com.example.mammabackend.domain.product.dto;

import com.example.mammabackend.domain.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ProductDto {

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductsParam {

        private String keyword;
        private Boolean isSale;

    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductsView {

        private Long productSq;
        private String name;
        private Long price;
        private String imagePath;
        private Boolean isSale;

        public static ProductsView fromEntity(Product product) {
            return ProductsView.builder()
                .productSq(product.getProductSq())
                .name(product.getName())
                .price(product.getPrice())
                .imagePath(product.getImagePath())
                .isSale(product.getIsSale())
                .build();
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductView {

        private Long productSq;
        private String code;
        private String name;
        private String description;
        private Long price;
        private Long stock;
        private String imagePath;
        private Boolean isSale;

        public static ProductView fromEntity(Product product, Long productStock) {
            return ProductView.builder()
                .productSq(product.getProductSq())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(productStock)
                .imagePath(product.getImagePath())
                .isSale(product.getIsSale())
                .build();
        }
    }

}
