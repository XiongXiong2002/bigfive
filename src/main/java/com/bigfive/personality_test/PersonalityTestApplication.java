package com.bigfive.personality_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PersonalityTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalityTestApplication.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("Admin123");
        System.out.println("加密后的管理员密码: " + encodedPassword);
	}

}
