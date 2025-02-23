package com.bigfive.personality_test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
/*@RestControllerAdvice 是 Spring Boot 默认支持的注解，用于全局异常处理。
它的作用是 捕获 @RestController 中的所有异常，并返回标准 HTTP 响应。*/ 

public class ExceptionHandlerController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        return buildErrorResponse(ex.getReason(), HttpStatus.valueOf(ex.getStatusCode().value()), ex);
    }
    

    // **处理所有未捕获的异常（500 内部错误）**
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return buildErrorResponse("Internal Server Error: "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    // **处理 400 请求参数错误**
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        System.out.println(ex);
        return buildErrorResponse("Bad Request", HttpStatus.BAD_REQUEST, ex);
    }

    // **统一错误返回格式**
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("details", ex.getMessage());

        return ResponseEntity.status(status).body(errorResponse);
    }
}
