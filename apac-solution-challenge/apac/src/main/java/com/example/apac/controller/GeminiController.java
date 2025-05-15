////package com.example.apac.controller;
////
////import org.springframework.ai.chat.ChatClient;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.bind.annotation.RestController;
////
////@RestController
////@RequestMapping(produces = "text/plain")
////public class GeminiController {
////
////    private final ChatClient chatClient;
////
////    public GeminiController(ChatClient chatClient) {
////        this.chatClient = chatClient;
////    }
////
////    @GetMapping(value = "/api/chat", produces = "application/json")
////    public String chat(@RequestParam(name = "prompt", defaultValue = "안녕하세요") String prompt) {
////        System.out.println("✅ 컨트롤러 도착! 프롬프트 = " + prompt);
////        try {
////            String result = chatClient.call(prompt);  // 단순 call만
////            System.out.println("✅ Gemini 응답: " + result);
////            return result;
////        } catch (Exception e) {
////            System.out.println("❌ Gemini 예외 발생!");
////            e.printStackTrace();
////            return "❌ Gemini 호출 중 오류 발생: " + e.getMessage();
////        }
////    }
////}
//
//package com.example.apac.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class GeminiController {
//
//    private final VertexAiGeminiChatClient geminiClient;
//
//    public GeminiController(VertexAiGeminiChatClient geminiClient) {
//        this.geminiClient = geminiClient;
//    }
//
//    @GetMapping("/api/chat")
//    public String chat(@RequestParam(name = "prompt", defaultValue = "안녕하세요") String prompt) {
//        System.out.println("✅ 컨트롤러 도착! 프롬프트 = " + prompt);
//
//        try {
//            Prompt request = new Prompt(prompt);  // Prompt 객체 생성
//            var response = geminiClient.call(request);
//            System.out.println("✅ Gemini 응답 객체: " + response);
//            return response.getResult().getOutput().getContent();
//        } catch (Exception e) {
//            System.out.println("❌ Gemini 예외 발생!");
//            e.printStackTrace();
//            return "❌ Gemini 호출 중 오류 발생: " + e.getMessage();
//        }
//    }
//}

package com.example.apac.controller;

import com.example.apac.dto.ChatRequest;
import com.example.apac.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
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
    public String chat(@RequestBody ChatRequest request) throws Exception {
        String prompt = request.getPrompt();
        String userId = request.getUserId();

        if (prompt == null || prompt.isBlank()) {
            return "프롬프트가 null이거나 비어 있습니다.";
        }

        System.out.println("컨트롤러 도착 - prompt: " + prompt + ", userId: " + userId);
        return geminiService.chatGemini(prompt, userId);
    }
}

