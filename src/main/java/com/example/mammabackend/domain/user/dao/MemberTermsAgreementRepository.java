package com.example.mammabackend.domain.user.dao;

import com.example.mammabackend.domain.user.domain.MemberTermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsAgreementRepository extends JpaRepository<MemberTermsAgreement, Long> {

}
