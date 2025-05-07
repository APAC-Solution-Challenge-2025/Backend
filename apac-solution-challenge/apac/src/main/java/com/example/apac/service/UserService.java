package com.example.apac.service;

import com.example.apac.domain.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore db = FirestoreClient.getFirestore();
    private static final String COLLECTION_NAME = "users";

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = db.collection(COLLECTION_NAME).document(email).get().get();
        if (doc.exists()) {
            return Optional.ofNullable(doc.toObject(User.class));
        }
        return Optional.empty();
    }

    public User save(User user) throws ExecutionException, InterruptedException {
        db.collection(COLLECTION_NAME).document(user.getEmail()).set(user).get();
        return user;
    }
}
