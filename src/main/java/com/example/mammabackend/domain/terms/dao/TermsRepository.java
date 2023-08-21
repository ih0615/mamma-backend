package com.example.mammabackend.domain.terms.dao;

import com.example.mammabackend.domain.terms.domain.Terms;
import com.example.mammabackend.domain.terms.domain.TermsCategory;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    default Optional<Terms> findPostedTerms(TermsCategory category) {
        LocalDateTime now = LocalDateTime.now();
        return findFirstByTermsCategoryAndPostAtLessThanEqualAndIsUsedIsTrueOrderByTermsSqDesc(
            category, now);
    }

    Optional<Terms> findFirstByTermsCategoryAndPostAtLessThanEqualAndIsUsedIsTrueOrderByTermsSqDesc(
        TermsCategory termsCategory, LocalDateTime now);

    List<Terms> findAllByTermsSqInAndIsUsedIsTrue(Collection<Long> termsSqs);
}
