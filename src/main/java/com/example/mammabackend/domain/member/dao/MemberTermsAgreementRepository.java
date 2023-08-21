package com.example.mammabackend.domain.member.dao;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.member.domain.MemberTermsAgreement;
import com.example.mammabackend.domain.terms.domain.Terms;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsAgreementRepository extends JpaRepository<MemberTermsAgreement, Long> {

    boolean existsByMemberAndTermsIn(Member member, Collection<Terms> termsList);
}
