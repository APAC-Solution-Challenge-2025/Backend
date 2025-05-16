package com.example.apac.calendar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyGoalAchieved {
    private String email;
    private String date;
    private int goalAchievedCount;
}
