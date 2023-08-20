package com.example.mammabackend.domain.order.dao;

import static com.example.mammabackend.domain.order.domain.QOrderDetail.orderDetail;
import static com.example.mammabackend.domain.product.domain.QProduct.product;

import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.domain.OrderDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailRepositoryImpl implements OrderDetailQueryDsl {

    private final JPAQueryFactory queryFactory;

    public OrderDetailRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<OrderDetail> findAllByOrderWithProduct(Order order) {
        return queryFactory.from(orderDetail)
            .select(orderDetail)
            .join(orderDetail.product, product).fetchJoin()
            .where(orderDetail.order.eq(order))
            .fetch();
    }
}
