package com.example.apac.service;

import com.example.apac.domain.User;
import com.example.apac.dto.HealthDataDto;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public Firestore getFirestore() {
        if (firestore == null) {
            firestore = FirestoreClient.getFirestore();
        }
        return firestore;
    }

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Firestore getDb() {
        return this.firestore;
    }

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        Firestore firestore = getDb();
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(email).get().get();
        if (doc.exists()) {
            return Optional.ofNullable(doc.toObject(User.class));
        }
        return Optional.empty();
    }

    public void saveHealthDataFromDto(String email, HealthDataDto request) throws Exception {
        HealthDataDto.HealthStatus status = request.getHealthStatus();

        Map<String, List<String>> selectedHealth = new HashMap<>();
        selectedHealth.put("userType", status.getUserType());
        selectedHealth.put("hobbies", status.getHobbies());
        selectedHealth.put("condition", status.getCondition());
        selectedHealth.put("birthDate", status.getBirthDate());
        selectedHealth.put("birthMethod", status.getBirthMethod());

        saveHealthStatus(email, selectedHealth);
    }

    private void saveHealthStatus(String email, Map<String, List<String>> selectedHealth) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("selectedHealth", selectedHealth);

        firestore.collection(COLLECTION_NAME)
                .document(email)
                .set(data)
                .get();
    }

    public User save(User user) throws Exception {
        getDb().collection(COLLECTION_NAME)
                .document(user.getEmail())
                .set(user)
                .get();

        return user;
    }

}
