package com.example.apac.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String provider;  // OAuth 제공자 (Google, Apple, Facebook)

    @Builder
    public User(String email, String name, String provider) {
        this.email = email;
        this.name = name;
        this.provider = provider;
    }
}
