package com.example.mammabackend.domain.order.api;

import com.example.mammabackend.domain.order.application.OrderService;
import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.domain.OrderDetail;
import com.example.mammabackend.domain.order.dto.OrderDto.OrderDetailView;
import com.example.mammabackend.domain.order.dto.OrderDto.OrderView;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersParam;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersView;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewProductStatistics;
import com.example.mammabackend.domain.order.dto.OrderDto.OrdersViewRepresentativeProduct;
import com.example.mammabackend.domain.order.dto.OrderDto.RegisterOrderParam;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.common.Response;
import com.example.mammabackend.global.common.Response.Body;
import com.example.mammabackend.global.exception.ResponseCodes;
import com.querydsl.core.QueryResults;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final Response response;
    private final Helper helper = Helper.getInstance();

    @PostMapping
    public ResponseEntity<Body> registerOrder(Principal principal,
        @Valid RegisterOrderParam request, BindingResult bindingResult) {

        bindingResult = request.verifyAdditional(bindingResult);

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        Long memberSq = helper.getMemberSq(principal);

        orderService.registerOrder(memberSq, request);

        return response.okMessage(ResponseCodes.SUCCESS_MESSAGE);
    }

    @PutMapping("/{orderSq}")
    public ResponseEntity<Body> confirmOrder(Principal principal, @PathVariable Long orderSq) {

        Long memberSq = helper.getMemberSq(principal);

        orderService.confirmOrder(orderSq, memberSq);

        return response.okMessage(ResponseCodes.SUCCESS_MESSAGE);
    }

    @DeleteMapping("/{orderSq}")
    public ResponseEntity<Body> cancelOrder(Principal principal, @PathVariable Long orderSq) {

        Long memberSq = helper.getMemberSq(principal);

        orderService.cancelOrder(orderSq, memberSq);

        return response.okMessage(ResponseCodes.SUCCESS_MESSAGE);
    }

    @GetMapping
    public ResponseEntity<Body> getOrders(Principal principal, OrdersParam request,
        @PageableDefault(sort = "orderSq", direction = Direction.DESC) Pageable pageable) {

        Long memberSq = helper.getMemberSq(principal);

        QueryResults<Order> qr = orderService.getOrders(memberSq, request, pageable);
        Map<Long, OrdersViewRepresentativeProduct> representativeProductMap = orderService.getRepresentativeProductMap(
            qr.getResults());
        Map<Long, OrdersViewProductStatistics> productStatisticsMap = orderService.getProductStatisticsMap(
            qr.getResults());

        List<OrdersView> results = qr.getResults().stream()
            .map(order -> OrdersView.fromEntity(order,
                representativeProductMap.get(order.getOrderSq()),
                productStatisticsMap.get(order.getOrderSq())))
            .toList();

        return response.ok(
            new QueryResults<>(results, qr.getLimit(), qr.getOffset(), qr.getTotal()));
    }

    @GetMapping("/{orderSq}")
    public ResponseEntity<Body> getOrder(Principal principal, @PathVariable Long orderSq) {

        Long memberSq = helper.getMemberSq(principal);

        Order order = orderService.getOrder(orderSq, memberSq);
        List<OrderDetail> orderDetails = orderService.getOrderDetailsWithProduct(order);
        List<OrderDetailView> orderDetailViews = orderDetails.stream()
            .map(OrderDetailView::fromEntity)
            .toList();

        return response.ok(OrderView.fromEntity(order, orderDetailViews));
    }
}
