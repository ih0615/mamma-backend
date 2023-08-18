package com.example.mammabackend.domain.user.dao;

import com.example.mammabackend.domain.user.domain.MemberSuspend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSuspendRepository extends JpaRepository<MemberSuspend, Long> {

}
