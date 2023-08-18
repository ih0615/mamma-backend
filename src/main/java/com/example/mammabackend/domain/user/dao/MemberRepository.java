package com.example.mammabackend.domain.user.dao;

import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.enums.MemberState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailAndStateNot(String email, MemberState state);

    Optional<Member> findByMemberSqAndState(Long id, MemberState state);

    Optional<Member> findByNameAndPhoneAndStateNot(String name, String phone, MemberState state);

    Optional<Member> findByEmailAndNameAndPhoneAndStateNot(String email, String name, String phone,
        MemberState state);

}
