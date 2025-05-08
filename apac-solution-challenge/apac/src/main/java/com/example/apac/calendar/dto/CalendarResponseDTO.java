package com.example.apac.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResponseDTO {
    private int year;
    private int month;
    private List<CalendarDayDTO> days;
}