package com.example.apac.calendar.controller;

import com.example.apac.calendar.dto.CalendarResponse;
import com.example.apac.calendar.service.CalendarService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
        Long userId = extractUserIdFromJwt(token);

        return calendarService.getCalendar(email, year, month);
    }

    private String extractEmailFromJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }
}