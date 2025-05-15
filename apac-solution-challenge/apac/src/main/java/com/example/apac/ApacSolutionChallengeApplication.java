package com.example.apac;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApacSolutionChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApacSolutionChallengeApplication.class, args);
	}
}
