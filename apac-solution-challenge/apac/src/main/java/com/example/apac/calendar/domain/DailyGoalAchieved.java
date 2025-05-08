package com.example.apac.calendar.domain;

import lombok.Data;

@Data
public class DailyGoalAchieved {
    private String email;
    private String date;
    private int goalAchievedCount;
}
