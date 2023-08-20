package com.example.mammabackend.domain.order.domain;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.order.enums.OrderState;
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
import java.util.Arrays;
import java.util.List;
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
@Entity(name = "order_tb")
public class Order extends CreatedAndUpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_sq")
    private Long orderSq;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_fk", referencedColumnName = "member_sq", nullable = false)
    private Member member;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "addressDetail")
    private String addressDetail;

    @Column(name = "message")
    private String message;

    @Column(name = "state")
    private OrderState state;

    public List<OrderDetail> generateOrderDetail(List<ProductQuantity> productQuantities) {

        return productQuantities.stream()
            .map(productQuantity -> OrderDetail.builder()
                .order(this)
                .product(productQuantity.getProduct())
                .name(productQuantity.getProduct().getName())
                .price(productQuantity.getProduct().getPrice())
                .quantity(productQuantity.getQuantity())
                .build()
            ).toList();
    }

    public void confirm() {
        this.state = OrderState.ORDER_COMPLETE;
    }

    public void cancel() {
        if (Arrays.asList(OrderState.PAYMENT_WAIT, OrderState.DELIVERY_WAIT).contains(this.state)) {
            this.state = OrderState.ORDER_CANCEL_COMPLETE;
        } else {
            this.state = OrderState.ORDER_CANCEL_WAIT;
        }
    }
}
