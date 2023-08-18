package com.example.mammabackend.domain.user.application;

import com.example.mammabackend.domain.user.application.interfaces.MemberService;
import com.example.mammabackend.domain.user.dao.MemberRepository;
import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.dto.MemberDto.FindMemberEmailParam;
import com.example.mammabackend.domain.user.dto.MemberDto.FindMemberPasswordParam;
import com.example.mammabackend.domain.user.dto.MemberDto.RegisterMemberParam;
import com.example.mammabackend.domain.user.dto.MemberDto.UpdateMemberParam;
import com.example.mammabackend.domain.user.enums.MemberState;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.common.email.dto.EmailDto.EmailMessage;
import com.example.mammabackend.global.common.email.service.EmailService;
import com.example.mammabackend.global.exception.ResponseCodes;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final Helper helper = Helper.getInstance();
    private final String VERIFY_EMAIL_REDIS_KEY = "verify:email:";
    private final int VERIFY_EMAIL_CODE_LENGTH = 6;
    private final long VERIFY_EMAIL_CODE_EXPIRE_SECOND = 600L;

    @Override
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmailAndStateNot(email, MemberState.WITHDRAW);
    }

    @Override
    public void verifyRequestEmail(String email) {

        String emailValidationCode = helper.generateRandomAlphanumericString(
            VERIFY_EMAIL_CODE_LENGTH);

        LocalDateTime expiredDateTime = LocalDateTime.now()
            .plusSeconds(VERIFY_EMAIL_CODE_EXPIRE_SECOND);

        emailService.sendMail(EmailMessage.builder()
            .to(email)
            .subject("이메일 인증 메일 입니다.")
            .message(
                String.format("인증 번호 : %s, 만료 일자 : %s", emailValidationCode, expiredDateTime.format(
                    DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"))))
            .build());

        redisTemplate.opsForValue().set(VERIFY_EMAIL_REDIS_KEY + email, emailValidationCode,
            VERIFY_EMAIL_CODE_EXPIRE_SECOND, TimeUnit.SECONDS);
    }

    @Override
    public boolean verifyConfirmEmail(String email, String emailValidationCode) {

        String cacheEmailValidationCode = redisTemplate.opsForValue()
            .get(VERIFY_EMAIL_REDIS_KEY + email);

        if (!StringUtils.hasText(cacheEmailValidationCode)) {
            return false;
        }

        return cacheEmailValidationCode.equals(emailValidationCode);
    }

    @Override
    public void registerMember(RegisterMemberParam request) {

        if (isDuplicatedEmail(request.getEmail())
            || !verifyConfirmEmail(request.getEmail(), request.getEmailValidationCode())) {
            throw new IllegalStateException(ResponseCodes.VALID_INVALID);
        }

        Member member = request.toEntity(passwordEncoder);

        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updateMember(Long memberSq, UpdateMemberParam request) {

        Member member = memberRepository.findByMemberSqAndState(memberSq, MemberState.NORMAL)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        member = request.toEntity(member);

        memberRepository.save(member);

    }

    @Transactional
    @Override
    public void withdrawMember(Long memberSq) {

        Member member = memberRepository.findByMemberSqAndState(memberSq, MemberState.NORMAL)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        member.withdraw();

        memberRepository.save(member);
    }

    @Override
    public Member getMemberInfo(Long memberSq) {

        return memberRepository.findByMemberSqAndState(memberSq, MemberState.NORMAL)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));
    }

    @Override
    public String findMemberEmail(FindMemberEmailParam request) {
        Member member = memberRepository.findByNameAndPhoneAndStateNot(request.getName(),
                request.getPhone(), MemberState.WITHDRAW)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        return member.getEmail();
    }

    @Transactional
    @Override
    public void findMemberPassword(FindMemberPasswordParam request) {
        Member member = memberRepository.findByEmailAndNameAndPhoneAndStateNot(request.getEmail(),
                request.getName(), request.getPhone(), MemberState.WITHDRAW)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        String tempPassword = helper.generateRandomAlphanumericString(10);

        emailService.sendMail(EmailMessage.builder()
            .to(member.getEmail())
            .subject("임시 비밀번호 발급")
            .message("임시 비밀번호 : " + tempPassword)
            .build());

        member.updatePassword(tempPassword, passwordEncoder);

        memberRepository.save(member);
    }
}
