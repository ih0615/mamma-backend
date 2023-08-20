package com.example.mammabackend.domain.terms.domain;

import com.example.mammabackend.global.common.audit.CreatedAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "terms_tb")
public class Terms extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_sq")
    private Long termsSq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_category_fk", referencedColumnName = "terms_category_sq", nullable = false)
    private TermsCategory termsCategory;

    @Column(name = "contents")
    private String contents;

    @Column(name = "post_at")
    private LocalDateTime postAt;

    @Default
    @Column(name = "is_used")
    private Boolean isUsed = true;
}
