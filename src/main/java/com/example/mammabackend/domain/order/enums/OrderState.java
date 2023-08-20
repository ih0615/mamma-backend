package com.example.mammabackend.domain.order.enums;

import com.example.mammabackend.global.common.enums.LegacyCode;
import com.example.mammabackend.global.common.enums.LegacyCodeConverter;

public enum OrderState implements LegacyCode {
    PAYMENT_WAIT(0),
    DELIVERY_WAIT(1),
    DELIVERY_PROCESS(2),
    DELIVERY_COMPLETE(3),
    ORDER_COMPLETE(4),
    ORDER_CANCEL_WAIT(5),
    ORDER_CANCEL_COMPLETE(6);

    private final int legacyCode;

    OrderState(int legacyCode) {
        this.legacyCode = legacyCode;
    }

    @Override
    public int getLegacyCode() {
        return this.legacyCode;
    }

    public static class OrderStateConverter extends LegacyCodeConverter<OrderState> {

        public OrderStateConverter() {
            super(OrderState.class);
        }
    }
}
