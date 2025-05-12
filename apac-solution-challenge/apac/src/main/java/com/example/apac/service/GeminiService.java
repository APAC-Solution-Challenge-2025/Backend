package com.example.apac.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final ChatClient chatClient;

    @Autowired
    public GeminiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chatWithGemini(String prompt) {
        return chatClient.call(prompt);
    }
}
