package com.example.mammabackend.domain.order.dao;

import static com.example.mammabackend.domain.order.domain.QOrder.order;
import static com.example.mammabackend.domain.order.domain.QOrderDetail.orderDetail;
import static com.example.mammabackend.domain.product.domain.QProduct.product;

import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewProductStatistics;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewRepresentativeProduct;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderQueryDsl {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public QueryResults<Order> findAllPagedWithOrderDetailAndProduct(Pageable pageable,
        List<Predicate> where, List<OrderSpecifier> orderBy) {

        Long total = queryFactory.from(order)
            .select(order.orderSq.countDistinct())
            .join(orderDetail).on(order.eq(orderDetail.order))
            .join(product).on(orderDetail.product.eq(product))
            .where(where.toArray(new Predicate[0]))
            .fetchFirst();

        List<Order> results = queryFactory.from(order)
            .select(order)
            .join(orderDetail).on(order.eq(orderDetail.order))
            .join(product).on(orderDetail.product.eq(product))
            .where(where.toArray(new Predicate[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderBy.toArray(new OrderSpecifier[0]))
            .groupBy(order)
            .fetch();

        return new QueryResults<>(results, (long) pageable.getPageSize(), pageable.getOffset(),
            total);
    }

    @Override
    public List<OrdersViewRepresentativeProduct> getRepresentativeProductMap(List<Order> orders) {

        return queryFactory.from(orderDetail)
            .select(Projections.constructor(OrdersViewRepresentativeProduct.class,
                order.orderSq,
                product.name,
                product.imagePath
            ))
            .join(order).on(orderDetail.order.eq(order))
            .join(product).on(orderDetail.product.eq(product))
            .where(orderDetail.orderDetailSq.in(
                    JPAExpressions.select(orderDetail.orderDetailSq.min())
                        .from(orderDetail)
                        .join(order).on(orderDetail.order.eq(order))
                        .where(order.in(orders))
                        .groupBy(order)
                )
            ).fetch();
    }

    @Override
    public List<OrdersViewProductStatistics> getProductStatisticsMap(List<Order> orders) {

        return queryFactory.from(orderDetail)
            .select(Projections.constructor(OrdersViewProductStatistics.class,
                order.orderSq,
                product.count(),
                product.price.sum()
            ))
            .join(order).on(orderDetail.order.eq(order))
            .join(product).on(orderDetail.product.eq(product))
            .where(orderDetail.order.in(orders))
            .groupBy(order)
            .fetch();
    }
}
