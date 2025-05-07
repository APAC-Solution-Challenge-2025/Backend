package com.example.apac.controller;

import com.example.apac.domain.User;
import com.example.apac.security.JwtUtil;
import com.example.apac.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Operation(summary = "Google OAuth 로그인", description = "Access Token으로 사용자 인증 후, 로그인 및 회원가입을 처리하고 JWT를 발급합니다. ")
    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogle(@RequestBody Map<String, String> body) throws ExecutionException, InterruptedException {
        String accessToken = body.get("accessToken");

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

        // 사용자 정보 받아옴
        Map userInfo = response.getBody();
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        User user = userService.findByEmail(email)
                .orElseGet(() -> {
                    try {
                        return userService.save(User.builder()
                                .email(email)
                                .name(name)
                                .provider("google")
                                .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        // JWT 토큰 생성
        String jwt = jwtUtil.generateToken(email);

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("email", email);
        result.put("name", name);

        return ResponseEntity.ok(result);
    }
}
