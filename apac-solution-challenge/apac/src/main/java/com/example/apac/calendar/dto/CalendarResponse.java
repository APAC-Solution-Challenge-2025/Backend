package com.example.apac.calendar.dto;

import java.util.List;

public record CalendarResponse(int year, int month, List<CalendarDay> days) {
}
