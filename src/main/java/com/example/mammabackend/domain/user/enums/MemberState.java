package com.example.mammabackend.domain.user.enums;

import com.example.mammabackend.global.common.enums.LegacyCode;
import com.example.mammabackend.global.common.enums.LegacyCodeConverter;

public enum MemberState implements LegacyCode {
    NORMAL(1),
    SUSPEND(2),
    WITHDRAW(3);

    private final int legacyCode;

    MemberState(int legacyCode) {
        this.legacyCode = legacyCode;
    }

    @Override
    public int getLegacyCode() {
        return this.legacyCode;
    }

    public static class MemberStateConverter extends LegacyCodeConverter<MemberState> {

        public MemberStateConverter() {
            super(MemberState.class);
        }
    }

}
