package com.example.mammabackend.domain.member.dao;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.member.domain.MemberShippingAddress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberShippingAddressRepository extends
    JpaRepository<MemberShippingAddress, Long> {

    Optional<MemberShippingAddress> findByMemberShippingAddressSqAndMember(
        Long memberShippingAddressSq, Member member);

    List<MemberShippingAddress> findAllByMember(Member member);

}
