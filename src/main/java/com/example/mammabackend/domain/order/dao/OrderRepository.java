package com.example.mammabackend.domain.order.dao;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.enums.OrderState;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryDsl {

    Optional<Order> findByOrderSqAndMember(Long orderSq, Member member);

    Optional<Order> findByOrderSqAndMemberAndState(Long orderSq, Member member, OrderState state);

    Optional<Order> findByOrderSqAndMemberAndStateNotIn(Long orderSq, Member member,
        Collection<OrderState> orderStates);
}
