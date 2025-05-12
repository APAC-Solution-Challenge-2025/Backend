//package com.example.apac.calendar.domain;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.EmbeddedId;
//import jakarta.persistence.Entity;
//import lombok.Getter;
//
//import java.time.LocalDate;
//
//@Entity
//public class DailyGoalAchieved {
//
//    @EmbeddedId
//    private DailyGoalAchievedId dailyGoalAchievedId;
//
//    @Getter
//    @Column(nullable = false)
//    private int goalAchievedCount = 0;
//
//    protected DailyGoalAchieved() {
//    }
//
//    protected DailyGoalAchieved(Long userId, LocalDate date, int goalAchievedCount) {
//        this.dailyGoalAchievedId = new DailyGoalAchievedId(userId, date);
//        this.goalAchievedCount = goalAchievedCount;
//    }
//
//    public DailyGoalAchievedId getId() {
//        return dailyGoalAchievedId;
//    }
//}
