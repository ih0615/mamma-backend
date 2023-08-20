package com.example.mammabackend.domain.member.dao;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.member.domain.MemberSuspend;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSuspendRepository extends JpaRepository<MemberSuspend, Long> {

    Optional<MemberSuspend> findByMemberAndIsAppliedIsTrue(Member member);
}
