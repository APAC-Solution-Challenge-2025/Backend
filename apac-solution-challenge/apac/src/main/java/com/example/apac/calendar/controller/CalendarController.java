package com.example.apac.calendar.controller;

import com.example.apac.calendar.dto.CalendarResponseDTO;
import com.example.apac.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public CalendarResponseDTO getCalendar(Authentication authentication, @RequestParam int year, @RequestParam int month) {
        String email = authentication.getName();

        return calendarService.getCalendar(email, year, month);
    }
}