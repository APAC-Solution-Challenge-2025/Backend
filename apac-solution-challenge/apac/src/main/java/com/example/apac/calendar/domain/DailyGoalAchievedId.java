package com.example.apac.calendar.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class DailyGoalAchievedId implements Serializable {
    private Long userId;
    private LocalDate date;

    protected DailyGoalAchievedId() {
    }

    protected DailyGoalAchievedId(Long userId, LocalDate date) {
        this.userId = this.userId;
        this.date = date;
    }
}
