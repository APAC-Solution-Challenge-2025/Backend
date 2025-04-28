package com.example.apac.calendar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class DailyGoalRecord {

    @EmbeddedId
    private DailyGoalRecordId dailyGoalRecordId;

    @Column(nullable = false)
    private int goalAchievedCount = 0;

    protected DailyGoalRecord() {
    }

    protected DailyGoalRecord(Long userId, LocalDate date, int goalAchievedCount) {
        this.dailyGoalRecordId = new DailyGoalRecordId(userId, date);
        this.goalAchievedCount = goalAchievedCount;
    }
}
