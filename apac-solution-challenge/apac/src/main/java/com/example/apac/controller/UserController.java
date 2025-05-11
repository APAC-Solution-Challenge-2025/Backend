package com.example.apac.controller;

import com.example.apac.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 정보 관련 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "현재 로그인된 사용자 정보 조회", description = "JWT 기반으로 현재 사용자 정보를 반환받습니다.")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) throws ExecutionException, InterruptedException {
        String email = (String) auth.getPrincipal();

        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }
}
