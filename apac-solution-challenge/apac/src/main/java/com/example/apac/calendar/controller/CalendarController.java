package com.example.apac.calendar.controller;

import com.example.apac.calendar.dto.CalendarResponse;
import com.example.apac.calendar.service.CalendarService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public CalendarResponse getCalendar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int year, @RequestParam int month) {
        Long userId = extractUserIdFromJwt(token);

        return calendarService.getCalendar(userId, year, month);
    }

    private Long extractUserIdFromJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("your-secret-key")
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.get("userId", String.class));
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }
}