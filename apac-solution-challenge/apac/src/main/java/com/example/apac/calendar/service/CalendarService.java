package com.example.apac.calendar.service;

import com.example.apac.calendar.domain.DailyGoalRecord;
import com.example.apac.calendar.dto.CalendarDay;
import com.example.apac.calendar.dto.CalendarResponse;
import com.example.apac.calendar.repository.DailyGoalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final DailyGoalRecordRepository dailyGoalRecordRepository;

    @Autowired
    public CalendarService(DailyGoalRecordRepository dailyGoalRecordRepository) {
        this.dailyGoalRecordRepository = dailyGoalRecordRepository;
    }

    public CalendarResponse getCalendar(Long userId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<DailyGoalRecord> records = dailyGoalRecordRepository.findByUserId_IdAndDateBetween(userId, startDate, endDate);

        List<CalendarDay> days = records.stream()
                .map(record -> new CalendarDay(record.getId().getDate().toString(), record.getGoalAchievedCount()))
                .collect(Collectors.toList());

        return new CalendarResponse(year, month, days);
    }
}
