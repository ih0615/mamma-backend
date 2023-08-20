package com.example.mammabackend.domain.order.domain;

import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductQuantity;
import com.example.mammabackend.global.common.audit.CreatedAndUpdatedAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "order_detail_tb")
public class OrderDetail extends CreatedAndUpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_sq")
    private Long orderDetailSq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_fk", referencedColumnName = "order_sq", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_fk", referencedColumnName = "product_sq", nullable = false)
    private Product product;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
