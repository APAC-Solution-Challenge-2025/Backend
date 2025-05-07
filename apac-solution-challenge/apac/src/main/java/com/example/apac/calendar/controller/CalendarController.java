package com.example.apac.calendar.controller;

import com.example.apac.calendar.dto.CalendarResponse;
import com.example.apac.calendar.service.CalendarService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    private final String secretKey;

    @Autowired
    public CalendarController(CalendarService calendarService, @Value("${jwt.secret.key}") String secretKey) {
        this.calendarService = calendarService;
        this.secretKey = secretKey;
    }

    @GetMapping
    public CalendarResponse getCalendar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int year, @RequestParam int month) {
        String email = extractEmailFromJwt(token);

        return calendarService.getCalendar(email, year, month);
    }

    private String extractEmailFromJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired JWT token");
        }
    }
}