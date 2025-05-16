package com.example.apac.controller;

import com.example.apac.dto.HealthDataDto;
import com.example.apac.service.HealthDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.auth.FirebaseAuth;

@RestController
@RequestMapping("/api")
@Tag(name = "HealthDataController", description = "회원가입 시 사용자 정보 관련 API")
public class HealthDataController {

    private final HealthDataService healthDataService;

//    @Autowired
    public HealthDataController(HealthDataService healthDataService) {
        this.healthDataService = healthDataService;
    }

    @Operation(summary = "회원가입 시 입력된 사용자 정보를 DB에 저장")
    @PostMapping("/health-data")
    public ResponseEntity<?> saveHealthData(@RequestBody HealthDataDto request,
                                                 @RequestParam("email") String email) {

        try {
            healthDataService.saveHealthDataFromDto(email, request);

            return ResponseEntity.ok("health status 데이터 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("!! health status 데이터 저장 실패: " + e.getMessage());
        }
    }
}

