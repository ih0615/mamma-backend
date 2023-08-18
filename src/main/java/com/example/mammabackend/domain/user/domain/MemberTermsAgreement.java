package com.example.mammabackend.domain.user.domain;

import com.example.mammabackend.domain.terms.domain.Terms;
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
@Builder
@Entity(name = "member_terms_agreement_tb")
public class MemberTermsAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_terms_agreement_sq")
    private Long memberTermsAgreementSq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_fk", referencedColumnName = "member_sq", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_fk", referencedColumnName = "terms_sq", nullable = false)
    private Terms terms;

}
