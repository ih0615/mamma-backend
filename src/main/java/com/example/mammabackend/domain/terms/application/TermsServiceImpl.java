package com.example.mammabackend.domain.terms.application;

import com.example.mammabackend.domain.member.application.interfaces.MemberService;
import com.example.mammabackend.domain.member.dao.MemberTermsAgreementRepository;
import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.member.domain.MemberTermsAgreement;
import com.example.mammabackend.domain.terms.dao.TermsCategoryRepository;
import com.example.mammabackend.domain.terms.dao.TermsRepository;
import com.example.mammabackend.domain.terms.domain.Terms;
import com.example.mammabackend.domain.terms.domain.TermsCategory;
import com.example.mammabackend.domain.terms.dto.TermsDto.TermsAgreementParam;
import com.example.mammabackend.global.exception.ProcessException;
import com.example.mammabackend.global.exception.ResponseCodes;
import com.example.mammabackend.global.exception.ValidException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;
    private final TermsCategoryRepository termsCategoryRepository;
    private final MemberService memberService;
    private final MemberTermsAgreementRepository memberTermsAgreementRepository;

    @Override
    public List<TermsCategory> getCategories() {
        return termsCategoryRepository.findAll();
    }

    @Override
    public Terms getPostedTerms(Long termsCategorySq) {
        TermsCategory category = termsCategoryRepository.findById(termsCategorySq)
            .orElseThrow(() -> new ProcessException(ResponseCodes.PROCESS_NOT_EXIST));

        return termsRepository.findPostedTerms(category)
            .orElseThrow(() -> new ProcessException(ResponseCodes.PROCESS_NOT_EXIST));
    }

    @Transactional
    @Override
    public void registerMemberAgreement(Long memberSq, TermsAgreementParam request) {
        Member member = memberService.findNormalMemberByMemberSq(memberSq);

        List<Terms> toAgreeTermsList = termsRepository.findAllByTermsSqInAndIsUsedIsTrue(
            request.getTermsSqs());

        if (request.getTermsSqs().size() != toAgreeTermsList.size()) {
            throw new ValidException("termsSqs", ResponseCodes.PROCESS_INVALID);
        }

        if (request.getTermsSqs().size() != toAgreeTermsList.stream()
            .mapToLong(terms -> terms.getTermsCategory().getTermsCategorySq())
            .count()) {
            throw new ValidException("termsSqs", ResponseCodes.PROCESS_INVALID);
        }

        boolean isAlreadyAgreed = memberTermsAgreementRepository.existsByMemberAndTermsIn(member,
            toAgreeTermsList);

        if (isAlreadyAgreed) {
            throw new ValidException("termsSqs", ResponseCodes.PROCESS_INVALID);
        }

        List<MemberTermsAgreement> memberTermsAgreements = toAgreeTermsList.stream()
            .map(terms -> MemberTermsAgreement.builder()
                .member(member)
                .terms(terms)
                .build())
            .toList();

        memberTermsAgreementRepository.saveAll(memberTermsAgreements);
    }
}
