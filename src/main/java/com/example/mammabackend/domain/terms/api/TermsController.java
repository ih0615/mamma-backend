package com.example.mammabackend.domain.terms.api;

import com.example.mammabackend.domain.terms.application.TermsService;
import com.example.mammabackend.domain.terms.domain.Terms;
import com.example.mammabackend.domain.terms.domain.TermsCategory;
import com.example.mammabackend.domain.terms.dto.TermsDto.TermsAgreementParam;
import com.example.mammabackend.domain.terms.dto.TermsDto.TermsCategoriesView;
import com.example.mammabackend.domain.terms.dto.TermsDto.TermsView;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.common.Response;
import com.example.mammabackend.global.common.Response.Body;
import com.example.mammabackend.global.exception.ResponseCodes;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/terms")
public class TermsController {

    private final TermsService termsService;
    private final Response response;
    private final Helper helper = Helper.getInstance();

    @GetMapping("/category")
    public ResponseEntity<Body> getTermsCategories() {

        List<TermsCategory> categories = termsService.getCategories();

        List<TermsCategoriesView> results = categories.stream()
            .map(TermsCategoriesView::fromEntity)
            .toList();

        return response.ok(results);
    }

    @GetMapping("/{termsCategorySq}")
    public ResponseEntity<Body> getTerms(@PathVariable Long termsCategorySq) {

        Terms terms = termsService.getPostedTerms(termsCategorySq);

        TermsView result = TermsView.fromEntity(terms);

        return response.ok(result);
    }

    @PostMapping
    public ResponseEntity<Body> registerMemberAgreement(Principal principal,
        @Valid TermsAgreementParam request, BindingResult bindingResult) {

        bindingResult = request.verifyAdditional(bindingResult);

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        Long memberSq = helper.getMemberSq(principal);

        termsService.registerMemberAgreement(memberSq, request);

        return response.okMessage(ResponseCodes.SUCCESS_MESSAGE);
    }

}
