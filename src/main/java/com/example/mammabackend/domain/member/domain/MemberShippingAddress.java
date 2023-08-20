package com.example.mammabackend.domain.member.domain;

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
@Entity(name = "Member_shipping_address_tb")
public class MemberShippingAddress extends CreatedAndUpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_shipping_address_sq")
    private Long memberShippingAddressSq;

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

}
