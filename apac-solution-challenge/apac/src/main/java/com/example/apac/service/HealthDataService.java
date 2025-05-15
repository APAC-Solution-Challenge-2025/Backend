package com.example.apac.service;

import com.example.apac.dto.HealthDataDto;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class HealthDataService {
    private final Firestore firestore;

    @Autowired
    public HealthDataService(Firestore firestore) {
        this.firestore = firestore;
    }

    public void saveHealthStatus(String email, Map<String, List<String>> selectedHealth) throws Exception {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("email", email);
        healthStatus.put("selectedHealth", selectedHealth);

        firestore.collection("users")
                .document(email)
                .set(healthStatus);
    }

    public void saveHealthDataFromDto(String email, HealthDataDto request) throws Exception {
        Map<String, List<String>> selectedHealth = request.getHealthStatus();
        saveHealthStatus(email, selectedHealth);
    }
}