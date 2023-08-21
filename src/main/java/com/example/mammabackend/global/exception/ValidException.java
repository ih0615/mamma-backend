package com.example.mammabackend.global.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidException extends RuntimeException {

    private final Map<String, String> errorMap = new LinkedHashMap<>();

    public ValidException(String key, String value) {
        super(key + " : " + value);
        errorMap.put(key, value);
    }

    public ValidException(Map<String, String> errors) {
        super(errors.entrySet().stream()
            .map(entrySet -> entrySet.getKey() + ":" + entrySet.getValue())
            .collect(Collectors.joining("\n")));
        errorMap.putAll(errors);
    }

    public Map<String, String> getErrorMap() {
        return this.errorMap;
    }
}
