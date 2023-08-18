package com.example.mammabackend.global.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class Response {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Body {

        private Object result;
        private String message;
        private Error error;
    }


    public ResponseEntity<Body> of(HttpStatus httpStatus, Object result, String message,
        Error error) {

        return ResponseEntity.status(httpStatus)
            .body(new Body(result, message, error));
    }

    public ResponseEntity<Body> ok(Object result, String message) {
        return of(HttpStatus.OK, result, message, null);
    }

    public ResponseEntity<Body> ok(Object result) {
        return of(HttpStatus.OK, result, null, null);
    }

    public ResponseEntity<Body> okMessage(String message) {
        return of(HttpStatus.OK, null, message, null);
    }

    public ResponseEntity<Body> fail(Object result, String message, Error error) {
        return of(HttpStatus.BAD_REQUEST, result, message, error);
    }

    public ResponseEntity<Body> fail(Object result, String message, String globalError,
        BindingResult error) {

        List<FieldError> fieldErrors = error.getFieldErrors();

        Error errors = Error.of(globalError, fieldErrors.stream()
            .filter(fieldError -> StringUtils.hasText(fieldError.getDefaultMessage()))
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));

        return fail(result, message, errors);
    }

    public ResponseEntity<Body> fail(Object result, String message, String globalError,
        Map<String, String> error) {

        Error errors = Error.of(globalError, error);

        return fail(result, message, errors);
    }

    public ResponseEntity<Body> fail(String message, Error error) {
        return fail(null, message, error);
    }

    public ResponseEntity<Body> fail(String message, String globalError,
        BindingResult error) {

        return fail(null, message, globalError, error);
    }

    public ResponseEntity<Body> fail(String message, String globalError,
        Map<String, String> error) {

        return fail(null, message, globalError, error);
    }

    public ResponseEntity<Body> fail(String globalError) {
        return fail(null, null, globalError, new LinkedHashMap<>());
    }

    public ResponseEntity<Body> fail(BindingResult error) {
        return fail(null, null, null, error);
    }

    public ResponseEntity<Body> fail(Map<String, String> error) {
        return fail(null, null, null, error);
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class Error {

        private String globalError;
        private Map<String, String> fieldError;

        public static Error of(String globalError, Map<String, String> fieldErrors) {
            return Error.builder()
                .globalError(globalError)
                .fieldError(fieldErrors)
                .build();
        }

        public static Error of(String globalError) {
            return Error.of(globalError, new LinkedHashMap<>());
        }

        public static Error of(Map<String, String> fieldErrors) {
            return Error.of("", fieldErrors);
        }
    }

}
