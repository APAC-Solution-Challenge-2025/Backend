package com.example.apac.service;

import com.example.apac.domain.DailyGoalAchieved;
import com.example.apac.dto.CalendarReportResponseDTO;
import com.example.apac.dto.CalendarResponseDTO;
import com.example.apac.dto.HealthDataDto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final UserService userService;
    private Firestore db;
    private static final String COLLECTION_NAME = "daily_goal_achieved";

    public CalendarService(UserService userService) {
        this.userService = userService;
    }

    public Firestore getDb() {
        if (db == null) {
            db = FirestoreClient.getFirestore();
        }
        return db;
    }

    public CalendarResponseDTO getCalendar(String email, int year, int month) {
        Firestore firestore = getDb();
        try {
            userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Fail to get user from Firebase", e);
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String start = startDate.format(formatter);
        String end = endDate.format(formatter);

        List<DailyGoalAchieved> result = new ArrayList<>();
        try {
            CollectionReference collection = firestore.collection(COLLECTION_NAME);

            Query query = collection
                    .whereEqualTo("email", email)
                    .whereGreaterThanOrEqualTo("date", start)
                    .whereLessThanOrEqualTo("date", end);

            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            System.out.println("Fetched document count: " + documents.size());

            for (QueryDocumentSnapshot doc : documents) {
                DailyGoalAchieved docData = doc.toObject(DailyGoalAchieved.class);
                result.add(docData);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Fail to fetch dailyGoalAchieved from Firestore");
        }

        List<HealthDataDto.CalendarDayDTO> days = result.stream()
                .map(docData -> new HealthDataDto.CalendarDayDTO(docData.getDate(), docData.getGoalAchievedCount()))
                .collect(Collectors.toList());

        return new CalendarResponseDTO(year, month, days);
    }

    public DailyGoalAchieved saveDailyGoal(DailyGoalAchieved dailyGoalAchieved) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        String docId = dailyGoalAchieved.getEmail() + "_" + dailyGoalAchieved.getDate();

        db.collection("daily_goal_achieved")
                .document(docId)
                .set(dailyGoalAchieved)
                .get();

        return dailyGoalAchieved;
    }

    public CalendarReportResponseDTO getCalendarReport(String email, int year, int month, int day) {
        String date = String.format("%04d-%02d-%02d", year, month, day);

    }
}
