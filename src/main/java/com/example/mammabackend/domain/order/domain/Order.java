package com.example.mammabackend.domain.order.domain;

import com.example.mammabackend.domain.order.enums.OrderState;
import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.domain.MemberShippingAddress;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_shipping_address_fk", referencedColumnName = "member_shipping_address_sq", nullable = false)
    private MemberShippingAddress memberShippingAddress;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "message")
    private String message;

    @Column(name = "state")
    private OrderState state;

}
