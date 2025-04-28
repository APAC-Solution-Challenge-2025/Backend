package com.example.apac.calendar.repository;

import com.example.apac.calendar.domain.DailyGoalAchieved;
import com.example.apac.calendar.domain.DailyGoalAchievedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyGoalAchievedRepository extends JpaRepository<DailyGoalAchieved, DailyGoalAchievedId> {
    Optional<DailyGoalAchieved> findById(DailyGoalAchievedId id);

    List<DailyGoalAchieved> findById_UserIdAndId_DateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
