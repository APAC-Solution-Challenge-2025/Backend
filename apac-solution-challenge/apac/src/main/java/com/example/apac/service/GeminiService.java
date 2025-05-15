package com.example.apac.service;

import com.example.apac.domain.ChatMessage;
import com.google.cloud.firestore.Firestore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class GeminiService {

    private final ChatClient chatClient;
    private final Firestore firestore;

    @Autowired
    public GeminiService(ChatClient chatClient, Firestore firestore) {
        this.chatClient = chatClient;
        this.firestore = firestore;
    }

    public String chatGemini(String prompt, String userId) throws Exception {
        // 사용자의 응답 저장
        ChatMessage userMessage = new ChatMessage(userId, "USER", prompt);
        saveChatMessage(userMessage);
        // Gemini의 응답
        String response = chatClient.call(prompt);

        // Gemini의 응답 저장
        ChatMessage geminiMessage = new ChatMessage(userId, "GEMINI", response);
        saveChatMessage(geminiMessage);

        return response;
    }

    public void saveChatMessage(ChatMessage message) throws Exception {
        firestore.collection("chat")
                .document(message.getUserId())
                .collection("messages")
                .add(message);
    }
}
