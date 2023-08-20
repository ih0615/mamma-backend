package com.example.mammabackend.domain.order.application;

import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.domain.OrderDetail;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersParam;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewProductStatistics;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewRepresentativeProduct;
import com.example.mammabackend.domain.order.dto.OrderDto.RegisterOrderParam;
import com.querydsl.core.QueryResults;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    void registerOrder(Long memberSq, RegisterOrderParam request);

    void confirmOrder(Long orderSq, Long memberSq);

    void cancelOrder(Long orderSq, Long memberSq);

    QueryResults<Order> getOrders(Long memberSq, OrdersParam request, Pageable pageable);

    Map<Long, OrdersViewRepresentativeProduct> getRepresentativeProductMap(List<Order> results);

    Map<Long, OrdersViewProductStatistics> getProductStatisticsMap(List<Order> results);

    Order getOrder(Long orderSq, Long memberSq);

    List<OrderDetail> getOrderDetailsWithProduct(Order order);
}
