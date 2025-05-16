package com.example.apac.controller;

import com.example.apac.domain.User;
import com.example.apac.security.JwtUtil;
import com.example.apac.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogle(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.badRequest().body("!! accessToken 없음");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.set("User-Agent", "SpringBoot-App");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map userInfo = response.getBody();
            if (userInfo == null) {
                return ResponseEntity.status(500).body("!! userInfo 응답이 비어 있음");
            }

            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            if (email == null || name == null) {
                return ResponseEntity.status(500).body("!! email, name이 없음");
            }

            User user = userService.findByEmail(email)
                    .orElseGet(() -> {
                        try {
                            return userService.save(User.builder()
                                    .email(email)
                                    .name(name)
                                    .provider("google")
                                    .build());
                        } catch (Exception e) {
                            throw new RuntimeException("!! 사용자 저장 실패: " + e.getMessage());
                        }
                    });

            String jwt = jwtUtil.generateToken(email);

            Map<String, Object> result = new HashMap<>();
            result.put("token", jwt);
            result.put("email", email);
            result.put("name", name);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("!! 서버 내부 오류: " + e.getMessage());
        }
    }
}
