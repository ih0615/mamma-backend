package com.example.mammabackend.domain.user.application.interfaces;

import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.dto.MemberDto.FindMemberEmailParam;
import com.example.mammabackend.domain.user.dto.MemberDto.FindMemberPasswordParam;
import com.example.mammabackend.domain.user.dto.MemberDto.RegisterMemberParam;
import com.example.mammabackend.domain.user.dto.MemberDto.UpdateMemberParam;
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
}
