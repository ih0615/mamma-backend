package com.example.mammabackend.domain.order.dao;

import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.domain.OrderDetail;
import java.util.List;

public interface OrderDetailQueryDsl {

    List<OrderDetail> findAllByOrderWithProduct(Order order);
}
