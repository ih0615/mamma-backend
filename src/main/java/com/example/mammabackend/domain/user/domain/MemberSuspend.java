package com.example.mammabackend.domain.user.domain;

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
@Entity(name = "member_suspend_tb")
public class MemberSuspend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_suspend_sq")
    private Long memberSuspendSq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_fk", referencedColumnName = "member_sq", nullable = false)
    private Member member;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Default
    @Column(name = "is_applied", nullable = false)
    private Boolean isApplied = true;

}
