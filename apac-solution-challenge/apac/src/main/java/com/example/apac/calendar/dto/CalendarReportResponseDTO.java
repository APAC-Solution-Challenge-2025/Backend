package com.example.apac.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CalendarReportResponseDTO {
    private String date;
    private List<HealthStatusSummaryDTO> healthStatus;
    private String recommendedSolution;
    private List<GoalStatusDTO> goals;
}
