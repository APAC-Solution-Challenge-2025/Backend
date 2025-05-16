package com.example.apac.calendar.controller;

import com.example.apac.calendar.domain.DailyGoalAchieved;
import com.example.apac.calendar.dto.CalendarReportResponseDTO;
import com.example.apac.calendar.dto.CalendarResponseDTO;
import com.example.apac.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

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

    @PostMapping
    public DailyGoalAchieved saveGoal(Authentication authentication,
                                      @RequestBody DailyGoalAchieved dailyGoalAchieved)
            throws ExecutionException, InterruptedException {
        dailyGoalAchieved.setEmail(authentication.getName());
        return calendarService.saveDailyGoal(dailyGoalAchieved);
    }

//    @GetMapping("/report")
//    public CalendarReportResponseDTO getCalendarReport(Authentication authentication, @RequestParam int year, @RequestParam int month, @RequestParam int day){
//        String email = authentication.getName();
//
//        return calendarService.getCalendarReport(email, year, month, day);
//    }
}