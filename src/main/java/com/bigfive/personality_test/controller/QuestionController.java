package com.bigfive.personality_test.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bigfive.personality_test.DTO.PersonalityScore;
import com.bigfive.personality_test.DTO.QuestionRequest;
import com.bigfive.personality_test.Server.QuestionService;
import com.bigfive.personality_test.entities.Question;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import io.github.bucket4j.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*") // 允许所有来源访问，或者指定前端地址
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // 存储不同用户的限流 Bucket（每个用户一个）
    private final Cache<String, Bucket> rateLimitersForQuestion = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES) // ⏳ 10 分钟不访问自动删除
            .maximumSize(1000) // 限制最多存 1000 个元素
            .build();

    private final Cache<String, Bucket> rateLimitersForResult = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    @PostMapping("/random")
    public ResponseEntity<List<Question>> getQuestions(@RequestBody QuestionRequest requestBody, HttpServletRequest request) {
        if (requestBody == null || requestBody.getState() == null) {
            throw new IllegalArgumentException("Request body or state cannot be null.");
        }

        String ipAddress = getClientIP(request);

        // 🔹 直接使用 `get(key, mappingFunction)` 避免重复调用 `getIfPresent`
        Bucket bucket = rateLimitersForQuestion.get(ipAddress, key -> createNewBucket());

        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests - Please slow down.");
        }

        List<Question> questions = questionService.getAllQuestions(requestBody.getState());
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/result")
    public ResponseEntity<?> getResult(@RequestBody List<PersonalityScore> requestBody, HttpServletRequest request) {
        if (requestBody == null) {
            throw new IllegalArgumentException("Request body or state cannot be null.");
        }

        String ipAddress = getClientIP(request);
        Bucket bucket = rateLimitersForResult.get(ipAddress, key -> createNewBucket());

        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests - Please slow down.");
        }

        return ResponseEntity.ok(questionService.setSimpleResult(requestBody));
    }

    @PostMapping("/getComplexResult")
    public ResponseEntity<String> getComplexResult(@RequestBody List<PersonalityScore> requestBody, HttpServletRequest request) {
        if (requestBody == null) {
            throw new IllegalArgumentException("Request body or state cannot be null.");
        }

        String ipAddress = getClientIP(request);
        Bucket bucket = rateLimitersForResult.get(ipAddress, key -> createNewBucket());

        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests - Please slow down.");
        }

        return ResponseEntity.ok(questionService.setComplexResult(requestBody));
    }

    // 🔹 使用 `Caffeine.get()` 之后，这个方法不需要 `ipAddress` 作为参数
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1))) // 每分钟最多 10 个请求
                .build();
    }

    // 用于获取用户 IP
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // 处理代理服务器
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
