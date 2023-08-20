package com.example.mammabackend.domain.member.application.interfaces;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.member.domain.MemberShippingAddress;
import com.example.mammabackend.domain.member.dto.MemberDto.FindMemberEmailParam;
import com.example.mammabackend.domain.member.dto.MemberDto.FindMemberPasswordParam;
import com.example.mammabackend.domain.member.dto.MemberDto.RegisterMemberAddressParam;
import com.example.mammabackend.domain.member.dto.MemberDto.RegisterMemberParam;
import com.example.mammabackend.domain.member.dto.MemberDto.UpdateMemberAddressParam;
import com.example.mammabackend.domain.member.dto.MemberDto.UpdateMemberParam;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    boolean isDuplicatedEmail(String email);

    void verifyRequestEmail(String email);

    boolean verifyConfirmEmail(String email, String emailValidationCode);

    void registerMember(RegisterMemberParam request);

    void updateMember(Long memberSq, UpdateMemberParam request);

    void withdrawMember(Long memberSq);

    Member getMemberInfo(Long memberSq);

    String findMemberEmail(FindMemberEmailParam request);

    void findMemberPassword(FindMemberPasswordParam request);

    void registerMemberShippingAddress(Long memberSq, RegisterMemberAddressParam request);

    void updateMemberShippingAddress(Long memberShippingAddressSq, Long memberSq,
        UpdateMemberAddressParam request);

    void deleteMemberShippingAddress(Long memberShippingAddressSq, Long memberSq);

    List<MemberShippingAddress> getMemberShippingAddresses(Long memberSq);

    Member findNormalMemberByMemberSq(Long memberSq);
}
