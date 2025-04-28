package com.example.apac.calendar.repository;

import com.example.apac.calendar.domain.DailyGoalAchieved;
import com.example.apac.calendar.domain.DailyGoalAchievedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyGoalAchievedRepository extends JpaRepository<DailyGoalAchieved, DailyGoalAchievedId> {
    Optional<DailyGoalAchieved> findById(DailyGoalAchievedId id);
}
