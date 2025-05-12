package com.example.apac.calendar.dto;

import java.util.List;

public record CalendarResponseDTO(int year, int month, List<CalendarDayDTO> days) {
}
