package com.example.apac.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoalStatusDTO {
    private String content;
    private boolean isAchieved;
}
