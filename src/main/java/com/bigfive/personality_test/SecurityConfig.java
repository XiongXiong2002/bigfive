package com.bigfive.personality_test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 关闭 CSRF 保护
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 允许所有请求访问
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话
            .formLogin(form -> form.disable()) // 关闭默认的表单登录
            .httpBasic(httpBasic -> httpBasic.disable()); // 关闭 HTTP Basic 认证

        return http.build();
    }
}
