package com.example.mammabackend.domain.terms.dao;

import com.example.mammabackend.domain.terms.domain.TermsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsCategoryRepository extends JpaRepository<TermsCategory, Long> {

}
