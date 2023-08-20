package com.example.mammabackend.domain.terms.dao;

import com.example.mammabackend.domain.terms.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms,Long> {

}
