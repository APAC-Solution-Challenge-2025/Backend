package com.example.apac.calendar.repository;

import com.example.apac.calendar.domain.DailyGoalRecord;
import com.example.apac.calendar.domain.DailyGoalRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyGoalRecordRepository extends JpaRepository<DailyGoalRecord, DailyGoalRecordId> {
    Optional<DailyGoalRecord> findById(DailyGoalRecordId id);
}
