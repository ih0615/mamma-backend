package com.example.mammabackend.domain.order.application;

import static com.example.mammabackend.domain.order.domain.QOrder.order;
import static com.example.mammabackend.domain.product.domain.QProduct.product;

import com.example.mammabackend.domain.member.application.interfaces.MemberService;
import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.order.dao.OrderDetailRepository;
import com.example.mammabackend.domain.order.dao.OrderRepository;
import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.domain.OrderDetail;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersParam;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewProductStatistics;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewRepresentativeProduct;
import com.example.mammabackend.domain.order.dto.OrderDto.RegisterOrderParam;
import com.example.mammabackend.domain.order.enums.OrderState;
import com.example.mammabackend.domain.product.application.ProductService;
import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.domain.product.dto.ProductDto.ProductQuantity;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.exception.ResponseCodes;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MemberService memberService;
    private final ProductService productService;
    private final Helper helper = Helper.getInstance();


    @Transactional
    @Override
    public void registerOrder(Long memberSq, RegisterOrderParam request) {

        Member member = memberService.findNormalMemberByMemberSq(memberSq);

        List<ProductQuantity> productQuantities = mergeProductAndQuantities(request.getProductSqs(),
            request.getQuantities());

        Map<Product, Long> productStockMap = productQuantities.stream()
            .map(ProductQuantity::getProduct).collect(Collectors.toMap(Function.identity(),
                product -> productService.getProductStock(product.getProductSq())));

        boolean isLackStock = productQuantities.stream()
            .anyMatch(productQuantity -> productStockMap.get(productQuantity.getProduct())
                < productQuantity.getQuantity());

        if (isLackStock) {
            throw new IllegalStateException(ResponseCodes.PROCESS_INVALID);
        }

        List<ProductQuantity> decrementProductQuantities = productQuantities.stream()
            .map(ProductQuantity::toNegate).toList();

        productService.incrementProductsStock(decrementProductQuantities);

        try {
            Order order = request.toEntity(generateOrderCode(member), member);
            orderRepository.save(order);

            List<OrderDetail> orderDetails = order.generateOrderDetail(productQuantities);
            orderDetailRepository.saveAll(orderDetails);
        } catch (Exception e) {
            productService.incrementCacheProductStocks(productQuantities);
            throw e;
        }
    }

    @Transactional
    @Override
    public void confirmOrder(Long orderSq, Long memberSq) {

        Member member = memberService.findNormalMemberByMemberSq(memberSq);

        Order order = orderRepository.findByOrderSqAndMemberAndState(orderSq, member,
                OrderState.DELIVERY_COMPLETE)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        order.confirm();

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderSq, Long memberSq) {
        Member member = memberService.findNormalMemberByMemberSq(memberSq);

        Order order = orderRepository.findByOrderSqAndMemberAndStateNotIn(orderSq, member,
                Arrays.asList(OrderState.ORDER_CANCEL_WAIT, OrderState.ORDER_CANCEL_COMPLETE))
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        order.cancel();

        orderRepository.save(order);

        if (order.getState().equals(OrderState.ORDER_CANCEL_COMPLETE)) {
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderWithProduct(order);
            List<ProductQuantity> productQuantities = orderDetails.stream()
                .map(ProductQuantity::fromEntity)
                .toList();
            productService.incrementProductsStock(productQuantities);
        }
    }

    @Override
    public QueryResults<Order> getOrders(Long memberSq, OrdersParam request, Pageable pageable) {

        Member member = memberService.findNormalMemberByMemberSq(memberSq);

        List<Predicate> where = new ArrayList<>();

        where.add(order.member.eq(member));
        if (StringUtils.hasText(request.getKeyword())) {
            where.add(product.name.contains(request.getKeyword()));
        }
        if (request.getStartDate() != null) {
            where.add(order.createdAt.goe(request.getStartDate().atStartOfDay()));
        }
        if (request.getEndDate() != null) {
            where.add(order.createdAt.lt(request.getEndDate().plusDays(1).atStartOfDay()));
        }

        List<OrderSpecifier> orderBy = helper.convertPageableToOrderSpecifier(pageable);

        return orderRepository.findAllPagedWithOrderDetailAndProduct(pageable, where, orderBy);
    }

    @Override
    public Map<Long, OrdersViewRepresentativeProduct> getRepresentativeProductMap(
        List<Order> orders) {

        List<OrdersViewRepresentativeProduct> qr = orderRepository.getRepresentativeProductMap(
            orders);

        return qr.stream()
            .collect(
                Collectors.toMap(OrdersViewRepresentativeProduct::getOrderSq, Function.identity()));
    }

    @Override
    public Map<Long, OrdersViewProductStatistics> getProductStatisticsMap(List<Order> orders) {

        List<OrdersViewProductStatistics> qr = orderRepository.getProductStatisticsMap(orders);

        return qr.stream()
            .collect(
                Collectors.toMap(OrdersViewProductStatistics::getOrderSq, Function.identity()));
    }

    @Override
    public Order getOrder(Long orderSq, Long memberSq) {

        Member member = memberService.findNormalMemberByMemberSq(memberSq);

        return orderRepository.findByOrderSqAndMember(orderSq, member)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));
    }

    @Override
    public List<OrderDetail> getOrderDetailsWithProduct(Order order) {

        return orderDetailRepository.findAllByOrderWithProduct(order);
    }

    private String generateOrderCode(Member member) {
        return "o-" + member.getMemberSq() + "-" + LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private List<ProductQuantity> mergeProductAndQuantities(List<Long> productSqs,
        List<Long> quantities) {

        List<Product> products = productService.getEnableOrderProductsByProductSqs(
            productSqs);

        if (productSqs.size() != products.size()) {
            throw new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST);
        }

        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductSq, Function.identity()));

        return IntStream.rangeClosed(0, productMap.size()).boxed()
            .map(index -> ProductQuantity.builder()
                .product(productMap.get(productSqs.get(index)))
                .quantity(quantities.get(index))
                .build())
            .toList();
    }
}
