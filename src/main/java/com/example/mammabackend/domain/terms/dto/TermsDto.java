package com.example.mammabackend.domain.terms.dto;

import com.example.mammabackend.domain.terms.domain.Terms;
import com.example.mammabackend.domain.terms.domain.TermsCategory;
import com.example.mammabackend.global.common.AddValid;
import com.example.mammabackend.global.exception.ResponseCodes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.BindingResult;

public class TermsDto {

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class TermsCategoriesView {

        private Long termsCategorySq;
        private String title;

        public static TermsCategoriesView fromEntity(TermsCategory termsCategory) {
            return TermsCategoriesView.builder()
                .termsCategorySq(termsCategory.getTermsCategorySq())
                .title(termsCategory.getTitle())
                .build();
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class TermsView {

        private Long termsSq;
        private Long termsCategorySq;
        private String contents;
        private LocalDateTime postAt;

        public static TermsView fromEntity(Terms terms) {
            return TermsView.builder()
                .termsSq(terms.getTermsSq())
                .termsCategorySq(terms.getTermsCategory().getTermsCategorySq())
                .contents(terms.getContents())
                .postAt(terms.getPostAt())
                .build();
        }
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TermsAgreementParam implements AddValid {

        @NotNull(message = ResponseCodes.VALID_REQUIRE)
        @Size(min = 1, message = ResponseCodes.VALID_REQUIRE)
        private List<Long> termsSqs;


        @Override
        public BindingResult verifyAdditional(BindingResult bindingResult) {

            if (bindingResult.hasErrors()) {
                return bindingResult;
            }

            if (this.termsSqs.stream().anyMatch(Objects::isNull)) {
                bindingResult.rejectValue("termsSqs", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            if (this.termsSqs.size() != new HashSet<>(this.termsSqs).size()) {
                bindingResult.rejectValue("termsSqs", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            return bindingResult;
        }
    }

}
