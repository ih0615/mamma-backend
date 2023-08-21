package com.example.mammabackend.global.common;

import static com.example.mammabackend.domain.product.domain.QProduct.product;

import com.example.mammabackend.global.exception.ProcessException;
import com.example.mammabackend.global.exception.ResponseCodes;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import java.security.Principal;
import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

public class Helper {

    private static final Helper instance = new Helper();

    private Helper() {

    }

    public static Helper getInstance() {
        return instance;
    }

    public Long getMemberSq(Principal principal) {

        if (principal == null) {
            throw new ProcessException(ResponseCodes.PROCESS_UNAUTHORIZED);
        }
        String name = principal.getName();
        if (!StringUtils.hasText(name)) {
            throw new ProcessException(ResponseCodes.PROCESS_UNAUTHORIZED);
        }
        return Long.valueOf(name);
    }

    public String generateRandomAlphanumericString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<OrderSpecifier> convertPageableToOrderSpecifier(Pageable pageable) {
        return pageable.getSort().stream().map(order -> {
            PathBuilder<?> pathBuilder = new PathBuilder<Object>(product.getType(),
                product.getMetadata());
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            return new OrderSpecifier(direction, pathBuilder.get(order.getProperty()));
        }).toList();
    }
}
