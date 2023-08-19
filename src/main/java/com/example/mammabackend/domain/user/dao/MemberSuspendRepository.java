package com.example.mammabackend.domain.user.dao;

import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.domain.MemberSuspend;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSuspendRepository extends JpaRepository<MemberSuspend, Long> {

    Optional<MemberSuspend> findByMemberAndIsAppliedIsTrue(Member member);
}
