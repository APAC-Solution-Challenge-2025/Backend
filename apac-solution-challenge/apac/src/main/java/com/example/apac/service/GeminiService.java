package com.example.apac.service;

import com.example.apac.domain.ChatMessage;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        DocumentSnapshot snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .get();

        StringBuilder contextPrompt = new StringBuilder();
        contextPrompt.append("""
        너는 산후우울감을 느끼는 사용자들을 위한 AI 챗봇이야. \n
        네가 해야하는 일은 사용자들의 건강상태, 취미, 좋아하는 것, 출산 후 지난 시간 및 출산 예정일 등의 정보를 기반으로
         사용자에게 적절한 목표 추천이나 심리적인 도움을 줘야 해.
         사용자가 너에게 목표 등을 추천해주길 바란다면, 다음의 건강 상태와 취미 정보를 기반으로 사용자에게 간단한 3가지 목표를 추천해줘.
         예를 들어, 사용자의 취미가 "커피 마시기, 영화보기"라면, 다음과 같이 응답(답변)을 줘. \n
         
         "오늘의 취미를 추천해드릴게요!
         1. 집 주변 카페에서 커피 1잔 마시기
         2. 영화 보러 가기
         3. 영화리뷰 영상 보고 영화 버킷리스트 작성하기 
         
         위의 목표들이 맘에 드신다면, "accept"를 눌러주세요!
         다른 목표들을 추천받고 싶다면, "decline"를 눌러주세요!"
         
         그 외에 사용자가 물어보는 다양한 것들에 대해서 사용자의 정보를 기반으로 조심스럽게 답변을 줘. \n\n
         
         
         
         You are an AI chatbot for users who feel postpartum depression. \n
         What you need to do is based on information such as users' health status, hobbies, favorite things, past time after giving birth, and expected delivery date
         You need to give users appropriate goal recommendations or psychological help.
         If you want users to recommend you goals, etc., please recommend three simple goals for users based on the following health conditions and hobby information.
         For example, if a user's hobby is "drinking coffee or watching a movie," give him/her a response. \n
         If there is a question in English, please answer it in English. \n
        
         "Let me recommend today's hobby!
         1. Drinking a cup of coffee at a cafe near my house
         2. Going to the movies
         3. Watch movie review videos and make a bucket list of movies
        
         If you like the above goals, please press "accept"!
         If you want to be recommended for other goals, please press "decline!"
        
         Other than that, please answer carefully based on the user's information about various things that the user asks.\n""");

        contextPrompt.append("사용자의 건강 상태 및 취미 정보는 다음과 같아: \n");

        if (snapshot.exists()) {
            Map<String, Object> healthData = snapshot.getData();
            for (Map.Entry<String, Object> entry : healthData.entrySet()) {
                contextPrompt.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            contextPrompt.append("(상태 정보 없음) \n");
        }

        contextPrompt.append("""
                위의 상태 정보를 바탕으로 다음 질문에 대답해줘. 만약 프롬프트에 질문이 영어로 들어온다면 영어로 대답해줘. 무조건!
                한글 프롬프트에는 한글로, 영어 프롬프트에는 영어로!!!!
                사용자의 상태정보에 대해서는 직접적으로 언급하지마.
                영어로 대답할 때도 사용자에게 힘이 되거나 심신의 안정을 취할 수 있는 말을 추가적으로 해줘.\n""");
        contextPrompt.append(prompt);

        ChatMessage userMessage = new ChatMessage(userId, "USER", prompt);
        saveChatMessage(userMessage);

        String response = chatClient.call(contextPrompt.toString());

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
