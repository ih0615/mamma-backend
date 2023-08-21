package com.example.mammabackend.domain.terms.application;

import com.example.mammabackend.domain.terms.domain.Terms;
import com.example.mammabackend.domain.terms.domain.TermsCategory;
import com.example.mammabackend.domain.terms.dto.TermsDto.TermsAgreementParam;
import java.util.List;

public interface TermsService {

    List<TermsCategory> getCategories();

    Terms getPostedTerms(Long termsCategorySq);

    void registerMemberAgreement(Long memberSq, TermsAgreementParam request);
}
