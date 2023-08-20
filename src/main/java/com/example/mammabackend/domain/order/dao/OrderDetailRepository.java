package com.example.mammabackend.domain.order.dao;

import com.example.mammabackend.domain.order.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>,OrderDetailQueryDsl {

}
