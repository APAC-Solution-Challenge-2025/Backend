package com.example.apac.service;

import com.example.apac.domain.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private Firestore db;
    private static final String COLLECTION_NAME = "users";

    public Firestore getDb() {
        if (db == null) {
            db = FirestoreClient.getFirestore();
        }
        return db;
    }

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        Firestore firestore = getDb();
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(email).get().get();
        if (doc.exists()) {
            return Optional.ofNullable(doc.toObject(User.class));
        }
        return Optional.empty();
    }

    public User save(User user) throws ExecutionException, InterruptedException {
        Firestore firestore = getDb();
        firestore.collection(COLLECTION_NAME).document(user.getEmail()).set(user).get();
        return user;
    }
}
