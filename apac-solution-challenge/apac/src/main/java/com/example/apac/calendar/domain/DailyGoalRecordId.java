package com.example.apac.calendar.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class DailyGoalRecordId implements Serializable {
    private Long userId;
    private LocalDate date;

    protected DailyGoalRecordId() {
    }

    protected DailyGoalRecordId(Long userId, LocalDate date) {
        this.userId = this.userId;
        this.date = date;
    }
}
