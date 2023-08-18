package com.example.mammabackend.global.common.enums;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public abstract class LegacyCodeConverter<T extends Enum<?> & LegacyCode> implements
    AttributeConverter<T, Integer> {

    private final Map<Integer, T> legacyCodeMap;

    public LegacyCodeConverter(Class<T> enumClass) {

        T[] enumConstants = enumClass.getEnumConstants();
        legacyCodeMap = Arrays.stream(enumConstants)
            .collect(Collectors.toMap(T::getLegacyCode, Function.identity()));
    }

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getLegacyCode();
    }

    @Override
    public T convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return legacyCodeMap.get(dbData);
    }
}
