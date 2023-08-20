package com.example.mammabackend.domain.order.dao;

import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewProductStatistics;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewRepresentativeProduct;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderQueryDsl {

    QueryResults<Order> findAllPagedWithOrderDetailAndProduct(Pageable pageable, List<Predicate> where, List<OrderSpecifier> orderBy);

    List<OrdersViewRepresentativeProduct> getRepresentativeProductMap(List<Order> orders);

    List<OrdersViewProductStatistics> getProductStatisticsMap(List<Order> orders);
}
