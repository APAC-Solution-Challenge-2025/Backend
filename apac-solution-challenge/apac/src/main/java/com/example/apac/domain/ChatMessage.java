package com.example.apac.domain;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String userId;
    private String role;  // User인지 AI인지 구분
    private String content;
    private Timestamp timestamp;

    public ChatMessage() {}

    public ChatMessage(String userId, String role, String content) {
        this.userId = userId;
        this.role = role;
        this.content = content;
        this.timestamp = Timestamp.now();
    }
}