package com.example.apac.controller;

import com.example.apac.dto.ChatRequest;
import com.example.apac.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "GeminiController", description = "Gemini 챗봇 API")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
    @Operation(summary = "Gemini 응답 생성 및 DB 저장", description = "사용자에게 받은 요청에 따른 Gemini 응답을 생성합니다. 그리고 Firebase에 사용자의 요청과 Gemini 응답을 저장합니다. ")
    @PostMapping("/chat")
    public String chat(Authentication authentication,  @RequestBody ChatRequest request) throws Exception {
        String prompt = request.getPrompt();
        String userId = authentication.getName();

        if (prompt == null || prompt.isBlank()) {
            return "프롬프트가 null이거나 비어 있습니다.";
        }

        System.out.println("컨트롤러 도착 - prompt: " + prompt + ", userId: " + userId);
        return geminiService.chatGemini(prompt, userId);
    }
}

