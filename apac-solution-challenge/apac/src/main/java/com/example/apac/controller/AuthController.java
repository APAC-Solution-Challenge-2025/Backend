package com.example.apac.controller;

import com.example.apac.domain.User;
import com.example.apac.repository.UserRepository;
import com.example.apac.security.JwtUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogle(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        // 사용자 정보 받아옴
        Map userInfo = response.getBody();
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .name(name)
                        .provider("google")
                        .build()));

        // JWT 토큰 생성
        String jwt = jwtUtil.generateToken(email); 

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("email", email);
        result.put("name", name);

        return ResponseEntity.ok(result);
    }
}
