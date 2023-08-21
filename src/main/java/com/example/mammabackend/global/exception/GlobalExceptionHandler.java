package com.example.mammabackend.global.exception;

import com.example.mammabackend.global.common.Response;
import com.example.mammabackend.global.common.Response.Body;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Component
public class GlobalExceptionHandler {

    private final Response response;

    @ExceptionHandler(ProcessException.class)
    public ResponseEntity<Body> processExceptionHandler(ProcessException e) {

        log.error("{} {}", e.getClass().getName(), e.getMessage());

        logStackTraceInThisPackage(e);

        return response.fail(e.getMessage());
    }

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<Body> validExceptionHandler(ValidException e) {

        Map<String, String> errorMap = e.getErrorMap();

        log.error("{} {}", e.getClass().getName(), e.getMessage());

        logStackTraceInThisPackage(e);

        return response.fail(errorMap);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Body> defaultExceptionHandler(Exception e) {

        log.error("", e);

        return response.fail(ResponseCodes.PROCESS_UNKNOWN);
    }

    private void logStackTraceInThisPackage(Exception e) {
        Arrays.stream(e.getStackTrace())
            .filter(stackTraceElement -> stackTraceElement.getClassName().startsWith("com.example"))
            .forEachOrdered(
                stackTraceElement -> log.error("{}.{}({}:{})", stackTraceElement.getClassName(),
                    stackTraceElement.getMethodName(), stackTraceElement.getFileName(),
                    stackTraceElement.getLineNumber()));
    }
}
