package com.example.mammabackend.domain.order.dto;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.order.domain.Order;
import com.example.mammabackend.domain.order.domain.OrderDetail;
import com.example.mammabackend.domain.order.enums.OrderState;
import com.example.mammabackend.domain.product.domain.Product;
import com.example.mammabackend.global.common.AddValid;
import com.example.mammabackend.global.common.Regexp;
import com.example.mammabackend.global.exception.ResponseCodes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.validation.BindingResult;

public class OrderDto {

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegisterOrderParam implements AddValid {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String name;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_PHONE, message = ResponseCodes.VALID_INVALID)
        private String phone;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_ZIPCODE, message = ResponseCodes.VALID_INVALID)
        private String zipcode;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String address;
        private String addressDetail;
        private String message;
        private List<Long> productSqs;
        private List<Long> quantities;

        @Override
        public BindingResult verifyAdditional(BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return bindingResult;
            }

            if (this.productSqs == null
                || this.productSqs.isEmpty()
                || this.productSqs.stream()
                .anyMatch(productSq -> productSq == null || productSq <= 0L)
                || this.productSqs.size() != new HashSet<>(this.productSqs).size()) {
                bindingResult.rejectValue("productSqs", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            if (this.quantities == null
                || this.quantities.isEmpty()
                || this.quantities.stream()
                .anyMatch(quantity -> quantity == null || quantity <= 0L)) {
                bindingResult.rejectValue("quantities", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            if (this.productSqs != null && this.quantities != null
                && this.productSqs.size() != this.quantities.size()) {
                bindingResult.rejectValue("quantities", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            return bindingResult;
        }

        public Order toEntity(String orderCode, Member member) {
            return Order.builder()
                .code(orderCode)
                .member(member)
                .name(this.name)
                .phone(this.phone)
                .zipcode(this.zipcode)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .message(this.message)
                .state(OrderState.PAYMENT_WAIT)
                .build();
        }
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrdersParam {

        @Getter
        private String keyword;
        @DateTimeFormat(iso = ISO.DATE)
        private LocalDate startDate;
        @DateTimeFormat(iso = ISO.DATE)
        private LocalDate endDate;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrdersView {

        private Long orderSq;
        private String title;
        private String imagePath;
        private Long totalPrice;
        private OrderState state;

        public static OrdersView fromEntity(Order order,
            OrdersViewRepresentativeProduct ordersViewRepresentativeProduct,
            OrdersViewProductStatistics ordersViewProductStatistics) {

            String title = ordersViewRepresentativeProduct.getName();
            if (ordersViewProductStatistics.getCount() > 1L) {
                title = title + "외 " + ordersViewProductStatistics.getCount() + "건";
            }

            return OrdersView.builder()
                .orderSq(order.getOrderSq())
                .title(title)
                .imagePath(ordersViewRepresentativeProduct.getImagePath())
                .totalPrice(ordersViewProductStatistics.getTotal())
                .state(order.getState())
                .build();

        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrdersViewRepresentativeProduct {

        private Long orderSq;
        private String name;
        private String imagePath;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrdersViewProductStatistics {

        private Long orderSq;
        private Long count;
        private Long total;
    }


    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrderView {

        private Long orderSq;
        private String code;
        private String name;
        private String phone;
        private String zipcode;
        private String address;
        private String addressDetail;
        private Long totalPrice;
        private String message;
        private OrderState state;
        private List<OrderDetailView> details;

        public static OrderView fromEntity(Order order, List<OrderDetailView> details) {
            return OrderView.builder()
                .orderSq(order.getOrderSq())
                .code(order.getCode())
                .name(order.getName())
                .phone(order.getPhone())
                .zipcode(order.getZipcode())
                .address(order.getAddress())
                .addressDetail(order.getAddressDetail())
                .totalPrice(details.stream().mapToLong(OrderDetailView::getPrice).sum())
                .message(order.getMessage())
                .state(order.getState())
                .details(details)
                .build();
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrderDetailView {

        private Long productSq;
        private String name;
        private String imagePath;
        private Long quantity;
        private Long price;

        public static OrderDetailView fromEntity(OrderDetail orderDetail) {
            Product product = orderDetail.getProduct();
            if (product == null) {
                return null;
            }

            return OrderDetailView.builder()
                .productSq(product.getProductSq())
                .name(orderDetail.getName())
                .imagePath(product.getImagePath())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .build();
        }
    }

}
